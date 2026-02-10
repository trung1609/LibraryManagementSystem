import { CardMembership, Dashboard, EventNote, Favorite, MenuBook, Person, Receipt, Settings } from "@mui/icons-material"
import React from "react"
import Reservations from "../Dashboard/Reservations";

export const secondaryItems = [
    {
        title: 'Profile',
        path: '/profile',
        icon: <Person/>
    },
    {
        title: 'Settings',
        path: '/settings',
        icon: <Settings/>
    }
];

export const navigationItems = [
    {
        title: 'Dashboard',
        path: '/',
        icon: <Dashboard/>,
        description: 'Overview & Stats',
    },
    {
        title: 'Browse Books',
        path: '/books',
        icon: <MenuBook/>,
        description: 'Explore Library',
    },
    {
        title: 'My Loans',
        path: '/my-loans',
        icon: <EventNote/>,
        description: 'Active & History',
        badge: 'loans'
    },
    {
        title: 'My Reservations',
        path: '/my-reservations',
        icon: <EventNote/>,
        description: 'Active & History',
        badge: 'reservations'
    },
    {
        title: 'My Fines',
        path: '/my-fines',
        icon: <Receipt/>,
        description: 'Pending & Paid',
        badge: 'fines'
    },
    {
        title: 'Subscriptions',
        path: '/subscriptions',
        icon: <CardMembership/>,
        description: 'Manage Plans',
        badge: 'subscriptions'
    },
    {
        title: 'Wishlist',
        path: '/wishlist',
        icon: <Favorite/>,
        description: 'Saved Books'
    }
]