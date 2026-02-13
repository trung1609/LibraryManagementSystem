import React from "react";
import bookImage from "../../img/book.png";
import { Box, Card, Tab, Tabs } from "@mui/material";
import { tabs } from "./tab";
import { loans } from "./loans";
import LoanCard from "./LoanCard";

const MyLoan = () => {
  const [activeTab, setActiveTab] = React.useState(0);

  const filteredLoans = React.useMemo(() => {
    const selectedTab = tabs[activeTab];

    if (selectedTab.value === null) {
      return loans;
    }

    return loans.filter((loan) => loan.status === selectedTab.value);
  }, [activeTab]);

  return (
    <div>
      <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-purple-500 py-8">
        <div className="px-4 sm:px-6 lg:px-8">
          <div className="mb-8">
            <h1 className="text-4xl font-bold text-gray-900 mb-4 flex items-center ">
              <span className="text-5xl">
                <img src={bookImage} alt="Book" className="w-10 h-10 mr-3" />
              </span>
              <span className="bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                My Borrowed Books
              </span>
            </h1>
            <p className="text-lg text-gray-600">
              Manage your book loans, track due dates, and renew books
            </p>
          </div>

          <Card className="mb-6" sx={{ overflow: "visible" }}>
            {" "}
            <Tabs
              value={activeTab}
              onChange={(e, val) => setActiveTab(val)}
              sx={{
                minHeight: 48,
                position: "relative",
                "&::after": {
                  content: '""',
                  position: "absolute",
                  bottom: 0,
                  left: 0,
                  right: 0,
                  height: "1px",
                  backgroundColor: "divider",
                  zIndex: 0,
                },
                "& .MuiTabs-indicator": {
                  height: 3,
                  bottom: 0,
                  borderRadius: "3px 3px 0 0",
                  zIndex: 1,
                },
                "& .MuiTab-root": {
                  minHeight: 48,
                  textTransform: "none",
                  fontWeight: 600,
                  pb: "12px",
                },
              }}
            >
              {tabs.map((tab) => (
                <Tab key={tab.Label} label={tab.Label} />
              ))}
            </Tabs>
          </Card>

          <div className="space-y-4">
            {filteredLoans.length > 0 ? (
              filteredLoans.map((loan) => (
                <LoanCard key={loan.id} loan={loan} />
              ))
            ) : (
              <div className="text-center py-12">
                <p className="text-gray-500 text-lg">
                  Không có sách nào trong danh mục này
                </p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyLoan;
