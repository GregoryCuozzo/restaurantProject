let closedDays = []

let inputs = document.getElementById("reservation").elements;
let inputRestaurant = inputs["restaurant"];
let strSelectedResto = inputRestaurant.value;
const today = new Date().toLocaleDateString()

$(document).ready(function () {
    $('.datepicker').datepicker({
        format: 'dd/mm/yyyy',
        todayHighlight: true,
        startDate: 'today',
        autoclose: true,
        daysOfWeekDisabled: [0, 2]
    });
})


let closedDays = []
console.log(document.getElementById("reservation"))
let inputs = document.getElementById("reservation").elements;
let inputRestaurant = inputs["restaurant"];
let strSelectedResto = inputRestaurant.value;

console.log(strSelectedResto)

const today = new Date().toLocaleDateString()


$(document).ready(function () {
    var options
    var date_input = $('input[name="date"]');
    var container = $('.bootstrap-iso form').length > 0 ? $('.bootstrap-iso form').parent() : "body";

    console.log(today)
    options = {
        format: "dd/mm/yyyy",
        container: container,
        todayHighlight: true,
        startDate: "today",
        autoclose: true,
        daysOfWeekDisabled: [0, 2]
    };
    console.log(date_input)
    date_input.datepicker(options)
    console.log(date_input.datepicker(options))
    console.log("----------------------------------------------------------------")
    console.log(options)
})


/*function getSelected() {
    var inputs = document.getElementById("reservation").elements;
    var inputRestaurant = inputs["restaurant"];
    var idSelectedResto = inputRestaurant.value;
    console.log(idSelectedResto)


    let closedDays = []
    let apiUrl = "/admin/reservation/getHoraire/" + idSelectedResto;
    // Récupérer tous les pokémons de l'api

    $.get(apiUrl, function () {
        //ceci se déclenche quand ma requete part
    })
        .done(function (result) {

            console.log("result :")
            console.log(result)


            let openDays = []
            for (let element of result) {
                let day = element.jour
                let openHour = element.ouverture;
                let closeHour = element.fermeture;
                openDays.push(day);
            }
            console.log("Opendays :")
            console.log(openDays)

            let days = [{key: 0, value: "Dimanche"},
                {key: 1, value: "Lundi"},
                {key: 2, value: "Mardi"},
                {key: 3, value: "Mercredi"},
                {key: 4, value: "Jeudi"},
                {key: 5, value: "Vendredi"},
                {key: 6, value: "Samedi"}];

            for (let element of days) {
                if (!openDays.includes(element.value)) {
                    closedDays.push(element.key);
                }
            }
            console.log("closedDAYS : ")
            console.log(closedDays)
            options = {
                format: "dd/mm/yyyy",
                container: container,
                todayHighlight: true,
                startDate: "today",
                autoclose: true,
                daysOfWeekDisabled: [5, 6]
            };
            date_input.datepicker(options);
            date_input.datepicker("refresh")
            console.log("date input 2222")
            console.log(date_input)
            $("#datePicker").datepicker("refresh");
            console.log("options : ")
            console.log(options)
        })


        .fail(function (error) {
            console.log('error', error);
        });

}*/


