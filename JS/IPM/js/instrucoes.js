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