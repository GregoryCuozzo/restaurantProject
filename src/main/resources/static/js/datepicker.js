

let closedDays = []
console.log(document.getElementById("reservation"))
let inputs = document.getElementById("reservation").elements;
let inputRestaurant = inputs["restaurant"];
let strSelectedResto = inputRestaurant.value;

console.log(strSelectedResto)

let today = new Date().toLocaleDateString()
console.log (today)
    $(document).ready(function(){
    var date_input=$('input[name="date"]'); //our date input has the name "date"
    var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
    var options={
    startDate: 'today',
    format: 'dd/mm/yyyy',
    container: container,
    todayHighlight: true,
    autoclose: true

};
    date_input.datepicker(options);
})





