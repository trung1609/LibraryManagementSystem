import { Box, Drawer } from "@mui/material";
import React from "react";
import SidebarDrawer from "./SidebarDrawer";

const drawerWith = "240px";
const UserSideBar = () => {
  return (
    <Box
      component={"nav"}
      sx={{ width: { md: drawerWith }, flexShrink: { md: 0 } }}
    >
      <Drawer
        variant="permanent"
        sx={{
          display: { xs: "none", md: "block" },
          "& .MuiDrawer-paper": {
            boxSizing: "border-box",
            width: drawerWith,
            border: "none",
          },
        }}
        open
      >
        <SidebarDrawer />
      </Drawer>
    </Box>
  );
};

export default UserSideBar;
