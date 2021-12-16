let closedDays = []
console.log(document.getElementById("reservation"))
let inputs = document.getElementById("reservation").elements;
let inputRestaurant = inputs["restaurant"];
let strSelectedResto = inputRestaurant.value;

console.log(strSelectedResto)

let today = new Date().toLocaleDateString()
console.log(today)
$(document).ready(function () {
    var date_input = $('input[name="date"]'); //our date input has the name "date"
    var container = $('.bootstrap-iso form').length > 0 ? $('.bootstrap-iso form').parent() : "body";
    var options = {
        startDate: "today",
        dateformat: "dd/mm/yyyy",
        container: container,
        todayHighlight: true,
        autoclose: true

    };
    date_input.datepicker(options);
})


function setHeure() {
    let heures = [];
    for (var i = 0; i <= 1425; i += 15) {
        hours = Math.floor(i / 60);
        minutes = i % 60;
        if (minutes < 10) {
            minutes = '0' + minutes;
        }
        heures.push(hours + ':' + minutes);
    }
    return heures
}

var select = document.getElementById("heure");
var options = setHeure()

for (var i = 0; i < options.length; i++) {
    var opt = options[i]
    var el = document.createElement("option");
    el.textContent = opt;
    el.value = opt;
    select.appendChild(el)
}




