import axios from "axios";
const URL = "http://localhost:8080";

export const getData = async (api) => {
    const response = await axios.get(URL + api);
    const data = response.data;
    return data;    
}