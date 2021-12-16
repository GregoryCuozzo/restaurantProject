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
