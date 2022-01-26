window.onload = function() {
    console.log("window loaded");
}

window.addEventListener('DOMContentLoaded', (event) => {
    console.log('DOM fully loaded and parsed');
    setStats();
});

// function colorText(){
//     console.log("EN LA FUNCION");
//     var perc_p = document.getElementById('grade-change');
//     // var percent = perc_p.textContent.split("%");
//     // var img = document.getElementById('grade-change-img');
//     if(perc_p >= 0) {
//         perc_p.classList.add("improve");
//         // img.src = "images/icons/GradeUP.svg";
//     }
//     else {
//         perc_p.classList.add("worsened");
//         // img.src = "images/icons/GradeDOWN.svg";
//     }
// }

function setStats() {
    var graph = document.getElementById('graph1');
    var slice1 = document.getElementById('graph_part1');
    var slice2 = document.getElementById('graph_part2');
    var texto = document.getElementById('texto');
    console.log('Texto ' + texto);

    var uno;
    var dos;
    // var dataNum = texto.getAttribute('data-after').slice(0, -1);
    var dataNum = texto.getAttribute('data-after');
    
    // console.log("Datanum: " + dataNum);

    if (dataNum < 50) {
        uno = dataNum/100*360+90;
        dos = 0;
        graph.style.background = '#46B6A0';
        slice1.style.background = '#e1e1e1';
        slice2.style.background = '#e1e1e1';
    }
    else {
        uno = 90;
        dos = dataNum/100*360;
        graph.style.background = '#e1e1e1';
        slice1.style.background = '#46B6A0';
        slice2.style.background = '#46B6A0';
    }
    
    slice1.style.transform = "rotate(" + uno + "deg)";
    slice2.style.transform = "rotate(" + dos + "deg)";

    texto.setAttribute('data-after', dataNum + '%');
}