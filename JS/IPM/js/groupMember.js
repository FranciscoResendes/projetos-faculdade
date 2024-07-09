window.addEventListener("load", main);

function main () {
    let p = JSON.parse(sessionStorage.getItem("activeGroupMember"));
    let img = document.getElementById("friendImg");
    document.getElementById("name").innerText = p.name;
    img.src = "imagens/" + p.name + ".png";
    document.getElementById("colorSquare").style.backgroundColor = 
                                                  "rgb("+p.color[0]+","+p.color[1]+","+p.color[2]+")";
    document.getElementById("color").innerText = "Cor de " + p.name;
    
    //friend remover
    document.getElementById("notMyBuddy").addEventListener("click",function(){
      let group = JSON.parse(sessionStorage.getItem("group"));
      let done = false;
      for (let i = 0; i < group.length && !done ; i++){
        if (group[i].name === p.name){
          group.splice(i,1);
          done = true;
        }
      }
      sessionStorage.setItem("group", JSON.stringify(group));
      if (group.length === 0){
        sessionStorage.setItem("hasGroup","false");
      }
      window.location.assign('Ecra_grupo.html')
    })

    document.getElementById("confirmationButton").addEventListener("click",function(){
      window.location.assign('Ecra_grupo.html')
    })
}






/** funções para voltar para trás */

let touchstartX = 0 //pos do rato quando clica no ecrã (eixo dos x)
let touchendX = 0 // pos do rato quando larga o botão (eixo dos x)
    
function checkDirection() {
  if (touchendX-70 > touchstartX) window.location.assign('Ecra_grupo.html');
}

document.addEventListener('mousedown', e => {
  touchstartX = e.pageX
})

document.addEventListener('mouseup', e => {
  touchendX = e.pageX
  checkDirection()
})

