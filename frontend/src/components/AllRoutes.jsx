import React from 'react'
import { Route, Routes } from "react-router"
import TestHomePage from './TestHomePage'
import ListingTable from "./ListingTable"
import PriceChart from "./PriceChart"

export default function AllRoutes() {
    return (
        <>
            <Routes>
                <Route path='/' element={<TestHomePage />}/>
                <Route path='/listing-table' element={<ListingTable />} />
                <Route path='/price-snapshots/:id' element={<PriceChart />} />
            </Routes>
        </>
    )
}
