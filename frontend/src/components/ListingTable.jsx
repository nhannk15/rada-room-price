import { useEffect } from 'react'
import { getData } from '../services/fetchApi';
import useListingStore from '../stores/listingStore';
import { Skeleton } from "@mui/material"

export default function ListingTable() {

    const listingStore = useListingStore();

    useEffect(() => {
        listingStore.fetchListing();
    }, []);

    const handleNext = () => {
        listingStore.fetchListing(listingStore.page + 1);
    }

    const handlePrev = () => {
        listingStore.fetchListing(listingStore.page - 1);
    }

    return (
        <>
            {listingStore.loading == true ? (
                <>
                    <Skeleton variant='rectangular' height={30} width={"100%"} style={{marginBottom: "10px"}}/>
                    <Skeleton variant='rectangular' height={30} width={"100%"} style={{marginBottom: "10px"}}/>
                    <Skeleton variant='rectangular' height={30} width={"100%"} style={{marginBottom: "10px"}}/>
                    <Skeleton variant='rectangular' height={30} width={"100%"} style={{marginBottom: "10px"}}/>
                </>
            ) : (
                <>
                    <div className="listing--list">
                        {listingStore.listings.map((item, index) => (
                            <div className="listing--item" key={item.id}>
                                <div className="listing--item__title">
                                    <h2>{item.title}</h2>
                                </div>
                            </div>
                        ))}
                    </div >
                    <h1>
                        Page: {listingStore.page + 1}
                    </h1>
                    <div>
                        <button disabled={listingStore.page <= 0 ? true : false} onClick={handlePrev}>
                            Prev
                        </button>
                        <button disabled={listingStore.page >= listingStore.totalPages - 1 ? true : false} onClick={handleNext}>
                            Next
                        </button>
                    </div>
                </>
            )
            }

        </>
    )
}
