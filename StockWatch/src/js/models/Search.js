import axios from 'axios';

export default class SimpleSearch{
    constructor(symbol){
        this.symbol = symbol;
    }


// export default class Search{
//     constructor(timesSeries, symbol, interval, size){
//         this.timesSeries = timesSeries;
//         this.symbol = symbol;
//         this.interval = interval;
//         this.size = size;
//     }
// }

async getResults() {
   const res = await axios(`https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=${this.symbol}&interval=5min&apikey=JZ9P3L165Y0TSGTV`);


   
   this.result = res.data;
    }
}
