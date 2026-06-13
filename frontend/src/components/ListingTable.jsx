import { useEffect } from 'react'
import { getData } from '../services/fetchApi';
import useListingStore from '../stores/listingStore';
import { Skeleton } from "@mui/material"
import { useNavigate } from 'react-router';

export default function ListingTable() {

    const listingStore = useListingStore();
    const navigate = useNavigate();

    useEffect(() => {
        listingStore.fetchListing();
    }, []);

    const handleNext = () => {
        listingStore.fetchListing(listingStore.page + 1);
    }

    const handlePrev = () => {
        listingStore.fetchListing(listingStore.page - 1);
    }

    const handleViewDetail = (id) => {
        navigate(`/price-snapshots/${id}`);                                                                                                                   
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
                                    <button onClick={() => handleViewDetail(item.id)}>
                                        Detail
                                    </button>
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
