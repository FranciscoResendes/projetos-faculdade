window.onload = function (){
    let hasFavorite = false;

    if(localStorage.getItem("aquaKidsFavorite") != null){
        hasFavorite = true;
        let div1 = document.getElementById("aquakidsFav");
        let img1 = document.createElement("img");

        img1.src = "imagens/aquakids.jpg";
        img1.width = 200;

        img1.style.position = 'relative';
        img1.style.left = '25%';
        img1.style.borderRadius = '5%';

        div1.appendChild(img1);

        let p1 = document.createElement("p");
        let txt1 = document.createTextNode("Aquakids");
        p1.appendChild(txt1);
        div1.appendChild(p1);
    }

    if(localStorage.getItem("slidesFavorite") != null){
        hasFavorite = true;
        let div2 = document.getElementById("slidesFav");
        let img2 = document.createElement("img");

        img2.src = "imagens/slides.jpg";
        img2.width = 200;

        img2.style.position = 'relative';
        img2.style.left = '25%';
        img2.style.borderRadius = '5%';

        div2.appendChild(img2);

        let p2 = document.createElement("p");
        let txt2 = document.createTextNode("Slides");
        p2.appendChild(txt2);
        div2.appendChild(p2);
    }

    if(localStorage.getItem("watercoasterFavorite") != null){
        hasFavorite = true;
        let div3 = document.getElementById("speedraceFav");
        let img3 = document.createElement("img");

        img3.src = "imagens/speedrace.jpg";
        img3.width = 200;

        img3.style.position = 'relative';
        img3.style.left = '25%';
        img3.style.borderRadius = '5%';

        div3.appendChild(img3);

        let p3 = document.createElement("p");
        let txt3 = document.createTextNode("Speedrace");
        p3.appendChild(txt3);
        div3.appendChild(p3);
    }

    if(localStorage.getItem("speedraceFavorite") != null){
        hasFavorite = true;
        let div4 = document.getElementById("watercoasterFav");
        let img4 = document.createElement("img");

        img4.src = "imagens/watercoaster.jpg";
        img4.width = 200;

        img4.style.position = 'relative';
        img4.style.left = '25%';
        img4.style.borderRadius = '5%';

        div4.appendChild(img4);

        let p4 = document.createElement("p");
        let txt4 = document.createTextNode("Watercoaster");
        p4.appendChild(txt4);
        div4.appendChild(p4);
    }

    //Criar texto de entrada
    let phrase = document.getElementById("intro");

    if(hasFavorite){
        let txt = document.createTextNode("Aqui estão as suas atrações favoritas!");
        phrase.appendChild(txt);
    }
    else{
        let txt = document.createTextNode("Não adicionou nenhuma atração aos favoritos!");
        phrase.appendChild(txt);
    }

    //Para o swipe
    let touchstartX = 0 //pos do rato quando clica no ecrã (eixo dos x)
    let touchendX = 0 // pos do rato quando larga o botão (eixo dos x)
        
    function checkDirection() {
    if (touchendX-50 > touchstartX) window.location.assign('Ecra_Principal.html');
    }

    document.addEventListener('mousedown', e => {
    touchstartX = e.pageX
    })

    document.addEventListener('mouseup', e => {
    touchendX = e.pageX
    checkDirection()
    })
}