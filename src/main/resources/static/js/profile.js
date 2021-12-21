window.onload = function(){
    let selectables = document.getElementsByClassName("selectable");
    for (let index = 0; index < selectables.length; index++) {
        selectables[index].addEventListener('click', selecter.bind(null, index));
    }
    selectables[0].classList.add("selected");
}

function selecter(id) {
    let selectables = document.getElementsByClassName("selectable");
    reset(selectables)
    selectables[id].classList.add("selected");
}

function reset(selectables) {
    for (let index = 0; index < selectables.length; index++) {
        selectables[index].classList.remove("selected");        
    }
}

