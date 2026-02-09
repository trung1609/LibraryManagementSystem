import { Box } from "@mui/material";
import React from "react";

const SidebarDrawer = () => {
  return (
    <Box
      sx={{
        height: "100%",
        display: "flex",
        flexDirection: "column",
        background: "linear-gradient(180deg, #1e293b 0%, #0f172a 100%)",
        color: "white",
        position: "relative",
        overflow: "hidden",
        "&::before": {
          content: '""',
          position: "absolute",
          top: 0,
          left: 0,
          right: 0,
          height: "300px",
          background:
            "radial-gradient(circle at 50% 0%, rgba(99, 102, 241, 0.15) 0%, transparent 100%)",
          pointerEvents: "none",
        },
      }}
    >
        <Box>
            
        </Box>
    </Box>
  );
};

export default SidebarDrawer;
