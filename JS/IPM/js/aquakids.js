window.onload = function (){
    if(localStorage.getItem("aquaKidsFavorite") != null){
      document.getElementById("favorite").src = "imagens/favoritefull.png";
    }
     document.getElementById("favorite").onclick = 
        function changeState(){
            var link = document.getElementById("favorite").src;
            if(link.indexOf("imagens/favorite.png") != -1){
                document.getElementById("favorite").src = "imagens/favoritefull.png";
                localStorage.setItem("aquaKidsFavorite", true);
            }
            else if (link.indexOf("imagens/favoritefull.png") != -1){
                document.getElementById("favorite").src = "imagens/favorite.png";
                localStorage.removeItem("aquaKidsFavorite");
            }
        }
}

let touchstartX = 0 //pos do rato quando clica no ecrã (eixo dos x)
let touchendX = 0 // pos do rato quando larga o botão (eixo dos x)
    
function checkDirection() {
  if (touchendX-50 > touchstartX) window.location.assign('atracoes.html');
}

document.addEventListener('mousedown', e => {
  touchstartX = e.pageX
})

document.addEventListener('mouseup', e => {
  touchendX = e.pageX
  checkDirection()
})