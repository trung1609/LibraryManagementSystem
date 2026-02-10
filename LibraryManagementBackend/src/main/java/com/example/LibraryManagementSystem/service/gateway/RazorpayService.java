package com.example.LibraryManagementSystem.service.gateway;

import com.example.LibraryManagementSystem.domain.PaymentType;
import com.example.LibraryManagementSystem.model.Payment;
import com.example.LibraryManagementSystem.model.SubscriptionPlan;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.response.PaymentLinkResponse;
import com.example.LibraryManagementSystem.service.SubscriptionPlanService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayService {

    private final SubscriptionPlanService subscriptionPlanService;
    @Value("${razorpay.key.id:}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret:}")
    private String razorpayKeySecret;

    @Value("${razorpay.callback.base-url:http://localhost:5173}")
    private String callbackBaseUrl;

    public PaymentLinkResponse createPaymentLink(Users users, Payment payment) throws RazorpayException {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            // Price is stored in INR (rupees). Razorpay expects amount in paisa (1 INR = 100 paisa)
            // Example: 99 INR -> 9900 paisa
            // Razorpay test mode max limit is around 50,000 INR (5,000,000 paisa)
            Long amountInPaisa = payment.getAmount() * 100;

            // Safety check for Razorpay limits (max 50,000 INR for test mode)
            if (amountInPaisa > 5000000) {
                throw new RazorpayException("Amount " + payment.getAmount() + " INR exceeds Razorpay test mode limit of 50,000 INR. Please update the subscription plan price.");
            }

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amountInPaisa);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("description", payment.getDescription());

            JSONObject customer = new JSONObject();
            customer.put("name", users.getFullName());
            customer.put("email", users.getEmail());
            if (users.getPhone() != null) {
                customer.put("contact", users.getPhone());
            }

            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            notify.put("sms", users.getPhone() != null);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("reminder_enable", true);

            String successUrl = callbackBaseUrl + "/payment/success/" + payment.getId();

            paymentLinkRequest.put("callback_url", successUrl);
            paymentLinkRequest.put("callback_method", "get");

            JSONObject notes = new JSONObject();
            notes.put("user_id", users.getId());
            notes.put("payment_id", payment.getId());

            if (payment.getPaymentType() == PaymentType.MEMBERSHIP) {
                notes.put("subscription_id", payment.getSubscription().getId());
                notes.put("plan", payment.getSubscription().getPlan().getPlanCode());
                notes.put("type", PaymentType.MEMBERSHIP);
            } else if (payment.getPaymentType() == PaymentType.FINE) {
//                    notes.put("fine_id", payment.getFine().getId());
                notes.put("type", PaymentType.FINE);
            }

            paymentLinkRequest.put("notes", notes);

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentUrl = paymentLink.get("short_url");
            String paymentLinkId = paymentLink.get("id");

            PaymentLinkResponse response = new PaymentLinkResponse();
            response.setPaymentLinkUrl(paymentUrl);
            response.setPaymentLinkId(paymentLinkId);

            return response;
        } catch (RazorpayException e) {
            throw new RazorpayException("Error creating Razorpay client: " + e.getMessage());
        }
    }

    public JSONObject fetchPaymentDetails(String paymentId) throws RazorpayException{
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            com.razorpay.Payment payment = razorpay.payments.fetch(paymentId);
            return payment.toJson();
        }catch (RazorpayException e){
            throw new RazorpayException("Error fetching payment details: " + e.getMessage());
        }
    }

    public boolean isValidPayment(String paymentId) throws RazorpayException {
        try {
            JSONObject paymentDetails = fetchPaymentDetails(paymentId);

            String status = paymentDetails.optString("status");
            // Amount from Razorpay is in paisa, our price is also stored in paisa
            long amountInPaisa = paymentDetails.optLong("amount");

            JSONObject notes = paymentDetails.getJSONObject("notes");

            String paymentType = notes.optString("type");


            if (!"captured".equalsIgnoreCase(status)) {
                return false;
            }

            if (paymentType.equals(PaymentType.MEMBERSHIP.toString())){
                String planCode = notes.optString("plan");
                SubscriptionPlan subscriptionPlan = subscriptionPlanService.getBySubscriptionPlanCode(planCode);

                return amountInPaisa == subscriptionPlan.getPrice();
            } else if (paymentType.equals(PaymentType.FINE.toString())) {
                Long findId = notes.getLong("fine_id");
                // to-do
            }
            return false;
        } catch (RazorpayException e) {
            throw new RazorpayException("Error validating payment: " + e.getMessage());
        }
    }

}
