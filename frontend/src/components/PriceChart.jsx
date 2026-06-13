import { useEffect, useState } from 'react'
import { getData } from "../services/fetchApi"
import { useParams } from 'react-router';
import { Line } from "react-chartjs-2"; 
import { Skeleton } from '@mui/material';

export default function PriceChart() {

    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(true);
    const { id } = useParams();
    const [roomTitle, setRoomTitle] = useState("");

    useEffect(() => {
        const fetchApi = async () => {
            await new Promise((resolve) => {
                setTimeout(() => {
                    resolve();
                }, 1000)
            })
            const response = await getData(`/api/listings/${id}/price-snapshots`);
            setLoading(false);
            setHistory(response.priceSnapshots);
            setRoomTitle(response.title);
        }
        fetchApi();
    }, [id]);

    const chartData = {
        labels: history.map((item) => item.snapshotAt?.slice(0, 10)),
        datasets: [
            {
                label: 'Giá VND',
                data: history.map((item) => item.price),
                borderColor: "rgb(75, 192, 192)",
                backgroundColor: "rgb(75, 192, 192, 0.1)",
                fill: true,
                tension: 0.3
            }
        ]
    };

    const options = {
        responsive: true,
        plugins: {
            legend: { position: 'bottom' },
            title: { display: true, text: roomTitle },
        },
        scales: {
            y: { beginAtZero: false },
        },
    };

    return (
        <>  
            {loading == true ? (
                <Skeleton variant='rounded' width="100%" height="80px"/>
            ) : (
                <Line data={chartData} options={options}/>
            )}
        </>
    )
}
