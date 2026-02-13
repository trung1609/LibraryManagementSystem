import {
  AccessAlarm,
  Book,
  CalendarMonth,
  CalendarToday,
  CheckCircle,
  Close,
  HourglassBottom,
} from "@mui/icons-material";
import React from "react";
import { getStatusColor } from "./getStatusColor";
import { Divider } from "@mui/material";
import { formatDate } from "../UserLayout/formatDate";

const MyReservationCard = ({ reservation }) => {
  const getStatusIcon = (status) => {
    const iconClass = "w-5 h-5";
    const icons = {
      PENDING: <HourglassBottom className={iconClass} />,
      AVAILABLE: <CalendarToday className={iconClass} />,
      FULFILLED: <CheckCircle className={iconClass} />,
      CANCELLED: <Close className={iconClass} />,
      EXPIRED: <AccessAlarm className={iconClass} />,
    };
    return icons[status] || <AccessAlarm className={iconClass} />;
  };
  const statusColors = getStatusColor(reservation.status);
  return (
    <div className="bg-white rounded-xl shadow-md hover:shadow-xl transition-all duration-300 hover:-translate-y-1 overflow-hidden border border-gray-100">
      <div
        className={`bg-gradient-to-br p-4 px-4 py-3 flex items-center justify-between ${statusColors.gradient}`}
      >
        <div className="flex items-center gap-2">
          <span>{getStatusIcon(reservation.status)}</span>

          <span
            className={`${statusColors.text} font-bold text-sm uppercase tracking-wider`}
          >
            {reservation.status}
          </span>
        </div>
      </div>
      <div className="p-6">
        <div className="mb-4">
          <div className="flex items-center gap-3 mb-2">
            <div className="p-3 rounded-lg bg-gradient-to-br from-indigo-500 to-purple-600 shadow-lg">
              <Book className="w-6 h-6 text-white" />
            </div>
            <div>
              <p>Book Id</p>
              <h3>#{reservation.bookId}</h3>
            </div>
          </div>

          <p>{reservation.bookTitle}</p>
        </div>

        <Divider/>

        <div className="space-y-3 mt-5">
          <div className="flex items-start gap-2">
            <AccessAlarm className="w-4 h-4 text-gray-500 uppercase" />
            <div>
              <p className="text-xs font-semibold text-gray-500 uppercase">
                Reserved
              </p>
              <p className="text-sm font-semibold text-gray-700">
                {formatDate(reservation.reservedAt)}
              </p>
            </div>
          </div>
        </div>

        {reservation.availableAt && (
          <div className="space-y-3 mt-5">
            <div className="flex items-start gap-2">
              <CalendarMonth className="w-4 h-4 text-green-500 uppercase" />
              <div>
                <p className="text-xs font-semibold text-green-500 uppercase">
                  Available
                </p>
                <p className="text-sm font-semibold text-green-700">
                  {formatDate(reservation.availableAt)}
                </p>
              </div>
            </div>
          </div>
        )}

        {reservation.fulfilledAt && (
          <div className="space-y-3 mt-5">
            <div className="flex items-start gap-2">
              <CheckCircle className="w-4 h-4 text-blue-500 uppercase" />
              <div>
                <p className="text-xs font-semibold text-blue-500 uppercase">
                  Fulfilled
                </p>
                <p className="text-sm font-semibold text-blue-700">
                  {formatDate(reservation.fulfilledAt)}
                </p>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default MyReservationCard;
