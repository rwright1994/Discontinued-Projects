import Search from './models/Search';
import SimpleSearch from './models/Search';
import * as searchView from './views/searchView'
import { elements } from './views/base';




const state = {};

google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(drawChart);

    

const controlSearch = async () => {

    const symbol = searchView.getInput();

    if(symbol){
        state.search = new SimpleSearch(symbol);

        try{
            await state.search.getResults();
        }catch(err){
            alert(err);
        }


        
    }
}

controlSearch();

function drawChart(){

    let data = google.visualization.arrayToDataTable([
        ['Time', 'Price'],
        ['12:00', 15.00],  
        ['12:05', 15.24],  
        ['12:10', 15.14],  
        ['12:15', 15.64],  
        ['12:20', 15.31] 
    ]);

    const options = {
        height: 200,
        width: 200,
        
        titleTextStyle: {
            color: 'white'
        },
        hAxis: {
            textStyle: {
                color: 'white'
            },
            titleTextStyle: {
                color: 'white'
            }
        },
        vAxis: {
            minValue: 0,
            maxValue: 16.00,
            textStyle: {
                color: 'white'
            },
            titleTextStyle: {
                color: 'white'
            }
        },
        title: 'Stock Price',
    
        backgroundColor: 'black',
        colors: ['#63f2ff'],
      
    };

    var chart = new google.visualization.AreaChart(document.getElementById('stock_chart'));
 

    chart.draw(data,options);

    // Add event listeners to button.


}

// elements.searchForm.addEventListener('click', e => {
//     e.preventDefault();
//     controlSearch();
//     console.log("Submit Clicked");
// });