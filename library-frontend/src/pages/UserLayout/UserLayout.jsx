import { Box, colors, Toolbar } from "@mui/material";
import React from "react";
import { Outlet } from "react-router-dom";
import UserSideBar from "./UserSideBar";

const drawerWith = 240;
const UserLayout = () => {
  return (
    <Box sx={{ display: "flex", minHeight: "100vh", bgcolor: "white" }}>
      {/* app bar */}

      {/* profile menu*/}

      {/* side bar */}
      <UserSideBar />

      {/* main content */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          width: { md: `calc(100% - ${drawerWith}px)` },
          minHeight: "100vh",
          p: 2,
        }}
      >
        <Toolbar />
        <Box>
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
};

export default UserLayout;
