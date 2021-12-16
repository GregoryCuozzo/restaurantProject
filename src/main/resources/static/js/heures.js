function setHeure() {
    let heures = [];
    for (var i = 0; i <= 1425; i += 15) {
        hours = Math.floor(i / 60);
        minutes = i % 60;
        if (minutes < 10) {
            minutes = '0' + minutes;
        }
        if (hours <10) {
            heures.push('0'+hours+':'+ minutes);
        }
        else
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

var selectF = document.getElementById("heureFermeture");
var choix = setHeure()

for (var j = 0; j < choix.length; j++) {
    var ch = choix[j]
    var il = document.createElement("option");
    il.textContent = ch;
    il.value = ch;
    selectF.appendChild(il)
}
