import { AppBar, Avatar, Box, IconButton, Toolbar, Tooltip, Typography } from '@mui/material'
import React from 'react'
import { navigationItems } from './NavigationItems';
import { isActive } from './util';
import { useLocation } from 'react-router';
import { Contrast, Menu as MenuIcon, Notifications, Search } from '@mui/icons-material';


const drawerWidth = 240;
const user = {
    fullName: "Vũ Minh Trung",
    profilePicture: "https://i.etsystatic.com/38592990/r/il/c42b46/4497455633/il_fullxfull.4497455633_3eth.jpg"
}
const Navbar = ({ handleDrawerToggle }) => {
    const location = useLocation();
    return (
        <AppBar position= "fixed" sx={{
            width: { md: `calc(100% - ${drawerWidth}px)` },
            ml: { md: `${drawerWidth}px` },
            bgcolor: "white",
            color: "text.primary",
            boxShadow: "0 1px 3px rgba(0,0,0,0.08)"
        }}>
            <Toolbar>
                <IconButton
                    color='inherit'
                    edge="start"
                    onClick={handleDrawerToggle}
                    sx={{ mr: 2, display: { md: "none" } }}
                >
                    <MenuIcon />
                </IconButton>

                <Typography
                    variant='h6'
                    noWrap
                    component='div'
                    sx={{ flexGrow: 1, fontWeight: 600 }}>
                    {navigationItems.find((item) => isActive(item.path, location))?.title || "Dashboard"}
                </Typography>

                <Tooltip title="Search">
                    <IconButton>
                        <Search />
                    </IconButton>
                </Tooltip>

                <Tooltip title="Notifications">
                    <IconButton>
                        <Notifications />
                    </IconButton>
                </Tooltip>

                <Tooltip title="Contrast">
                    <IconButton sx={{ ml: 2 }}>
                        <Contrast />
                    </IconButton>
                </Tooltip>

                <Tooltip title="Account">
                    <IconButton sx={{ ml: 1 }}>
                        <Avatar src={user?.profilePicture} sx={{ width: 36, height: 36 }}>
                            {user?.fullName?.charAt(0)}
                        </Avatar>
                    </IconButton>
                </Tooltip>
            </Toolbar>
        </AppBar>
    )
}

export default Navbar
