window.onload = function() {
  
  let map = createMap('map', 37.093434, -8.073272, 13); 
  L.marker([37.093434, -8.073272]).addTo(map)
    .bindPopup('Aquashow')
    .openPopup();

}

function createMap(containerId, lat, lng, zoom) {
  var map = L.map(containerId).setView([lat, lng], zoom);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors',
    maxZoom: 19,
  }).addTo(map);

  return map;
}
