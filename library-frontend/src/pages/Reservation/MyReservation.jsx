import React, { useMemo } from "react";
import bookImage from "../../img/open-book.png";
import { AccessAlarm, Book, CalendarToday } from "@mui/icons-material";
import { tabs } from "./tab";
import { myReservation } from "./reservation";
import MyReservationCard from "./MyReservationCard";

const MyReservation = () => {
  const state = { total: 6, active: 2, available: 1 };
  const [activeTab, setActiveTab] = React.useState(0);

  // Logic lọc reservation theo tab (Dùng useMemo để tối ưu)
  const filteredReservations = useMemo(() => {
    if (activeTab === 1) {
      // Tab Active (index 1): PENDING hoặc AVAILABLE
      return myReservation.filter(
        (r) => r.status === "PENDING" || r.status === "AVAILABLE",
      );
    } else if (activeTab === 2) {
      // Tab Completed (index 2): FULFILLED
      return myReservation.filter((r) => r.status === "FULFILLED");
    }
    // Tab All (index 0)
    return myReservation;
  }, [activeTab]);

  return (
    <div className="min-h-screen py-8">
      <div className="px-4 sm:px-6 lg:px-8">
        {/* Header Section */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-4 flex items-center ">
            <span className="text-5xl">
              <img src={bookImage} alt="Book" className="w-10 h-10 mr-3" />
            </span>
            <span className="bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
              My Reservations
            </span>
          </h1>
          <p className="text-lg text-gray-600">
            Manage and track your book reservations
          </p>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <div className="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 pl-4 pr-6 py-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-semibold text-gray-500 uppercase tracking-wide">
                  Total Reservations
                </p>
                <p className="text-4xl font-extrabold text-gray-900 mt-1">
                  {state.total}
                </p>
              </div>
              <div className="p-4 bg-gradient-to-br from-indigo-500 to-purple-600 rounded-xl shadow-lg">
                <Book className="w-8 h-8 text-white" />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 pl-4 pr-6 py-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-semibold text-gray-500 uppercase tracking-wide">
                  Active Reservations
                </p>
                <p className="text-4xl font-extrabold text-gray-900 mt-1">
                  {state.active}
                </p>
              </div>
              <div className="p-4 bg-gradient-to-br from-yellow-500 to-amber-500 rounded-xl shadow-lg">
                <AccessAlarm className="w-8 h-8 text-white" />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 pl-4 pr-6 py-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-semibold text-gray-500 uppercase tracking-wide">
                  Ready To Pick Up
                </p>
                <p className="text-4xl font-extrabold text-gray-900 mt-1">
                  {state.available}
                </p>
              </div>
              <div className="p-4 bg-gradient-to-br from-green-500 to-emerald-400 rounded-xl shadow-lg">
                <CalendarToday className="w-8 h-8 text-white" />
              </div>
            </div>
          </div>
        </div>

        {/* Tabs Navigation */}
        <div className="bg-white rounded-xl shadow-lg mb-6 overflow-hidden">
          <div className="flex border-b border-gray-200">
            {tabs.map((tab, index) => (
              <button
                key={index}
                onClick={() => setActiveTab(index)}
                className={`flex-1 px-6 py-4 font-semibold text-base flex items-center justify-center gap-2 transition-all ${
                  activeTab === index
                    ? "text-indigo-600 border-b-4 border-indigo-600 bg-indigo-50"
                    : "text-gray-600 hover:bg-gray-50"
                }`}
              >
                {tab.icon}
                {tab.label}
              </button>
            ))}
          </div>
        </div>

        {/* Reservations Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredReservations.length > 0 ? (
            filteredReservations.map((reservation) => (
              <MyReservationCard
                key={reservation.id}
                reservation={reservation}
              />
            ))
          ) : (
            <div className="col-span-full text-center py-10 text-gray-500">
              No reservations found in this category.
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MyReservation;
