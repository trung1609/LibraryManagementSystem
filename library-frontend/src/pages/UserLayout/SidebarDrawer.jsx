import { MenuBook } from "@mui/icons-material";
import { Avatar, Box, Typography } from "@mui/material";
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
      <Box sx={{
        p: 3, display: "flex",
        alignItems: "center",
        gap: 2,
        position: "relative",
        zIndex: 1
      }}>
        <Box>
          <Avatar >
            <MenuBook />
          </Avatar>
        </Box>
        <Box>
          <Typography sx={{
            fontSize: 800,
            letterSpacing: 0.5,
            background: "linear-gradient(135deg, #ffffff 0%, #e0e7ff 100%)",
            backgroundClip: "text",
            WebkitBackgroundClip: "text",
            WebkitTextFillColor: "transparent"
          }}>
            TrungBook
          </Typography>
          <Typography
            variant="caption"
            sx={
              {
                opacity: 0.7,
                fontWeight: 500,
                letterSpacing: 1,
                textTransform: 'uppercase'
              }
            }>
              Library Hub
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default SidebarDrawer;
