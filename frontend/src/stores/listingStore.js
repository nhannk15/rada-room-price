import {
    create
} from "zustand"
import {
    getData
} from "../services/fetchApi";

const useListingStore = create((set) => ({
    listings: [],
    page: 0,
    totalPages: 0,
    loading: true,
    fetchListing: async (page = 0) => {
        try {
            const data = await getData(`/api/listings?page=${page}&size=10`);

            await new Promise((resolve) => {
                setTimeout(() => {
                    resolve();
                }, 2000);
            });
            set({
                listings: data.content,
                page,
                totalPages: data.totalPages,
                loading: false
            });
        } catch (error) {
            console.log("Fetch error: " + error);
            set({
                loading: false
            })
        }
    },
    createListing: async (data) => {
        console.log("Create Lising");
    },
    updateListing: async (data) => {
        console.log("Update listing");
    },
    deleteListing: async (data) => {
        console.log("Delete listing");
    }
}));

export default useListingStore;