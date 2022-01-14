window.onload = function() {
    var fileName = location.href.split("/").slice(-1); 
    if(fileName == "session_review.html") {
        colorText();
    }
}

function colorText(){
    var perc_p = document.getElementById('grade-change');
    var percent = perc_p.textContent.split("%");
    var img = document.getElementById('grade-change-img');
    if(percent[0] >= 0) {
        perc_p.classList.add("improve");
        img.src = "images/icons/GradeUP.svg";
    }
    else {
        perc_p.classList.add("worsened");
        img.src = "images/icons/GradeDOWN.svg";
    }
}

function setStats(nuevo) {
    var graph = document.getElementById('graph1');
    var slice1 = document.getElementById('graph_part1');
    var slice2 = document.getElementById('graph_part2');
    var texto = document.getElementById('texto');
    console.log('Texto ' + texto);
    console.log('Nuevo ' + nuevo);

    var uno;
    var dos;
    texto.setAttribute('data-after', nuevo + '%');
    var dataNum = texto.getAttribute('data-after').slice(0, -1);

    if (nuevo < 50) {
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
}