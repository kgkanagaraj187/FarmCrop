var kmap1;
var kmap2;

document.onreadystatechange = function() {
  if(document.readyState === 'complete'){
    /*kmap1 = new KMaps({ // Code for Open Street.
      mapControlId: 'kmapId1',
      style: { border: 'gray solid 3px' },
      timeline: {},
      // provider: 'Esri.WorldImagery'
    });*/
	  
	  kmap1 = new KMaps({
	      mapControlId: 'kmapId1',
	      style: { border: 'gray solid 3px' },
	      timeline: {},
	      provider: 'GoogleMaps.Hybrid' //Other Values For Providers: 'GoogleMaps.Hybrid', 'GoogleMaps.Streets', 'GoogleMaps.Satellite', 'GoogleMaps.Terrain'
	    });
    console.debug(kmap1);
  }
};

var sync_flag = 0;

function sync(kmap1, kmap2) {

  if (sync_flag == 1){
    // document.getElementById("sync").innerHTML = sync_flag;
    return;
  }else{
    console.log('Doing sync...');
    // document.getElementById("sync").innerHTML = sync_flag;
    var kmap1TimeLine = kmap1.getTimeLineObj();
    var kmap2TimeLine = kmap2.getTimeLineObj();

    kmap1TimeLine.on('click', function (event){
      setDestKMapTime(kmap1,kmap2);
    });

    kmap2TimeLine.on('click', function (event) {
      setDestKMapTime(kmap2,kmap1);
    });

    kmap1TimeLine.on('select', function (event) {
      setDestKMapTime(kmap1,kmap2);
    });

    kmap2TimeLine.on('select', function (event) {
      setDestKMapTime(kmap2,kmap1);
    });

    kmap1TimeLine.on('timechange', function (event) {
      setDestKMapTime(kmap1,kmap2);
    });

    kmap2TimeLine.on('timechange', function (event) {
      setDestKMapTime(kmap2,kmap1);
    });

    sync_flag = 1;
  }


}

function setDestKMapTime(sourceKMap, destKMap) {
  var sourceTime = sourceKMap.getTime();
  var x1 = sourceKMap.getStartTime();
  var y1 = sourceKMap.getEndTime();

  var x2 = destKMap.getStartTime();
  var y2 = destKMap.getEndTime();

  var destTime = (x2 + (y2 - x2) / (y1 - x1) * (sourceTime - x1));
  console.log('Source Parcel-Id -> Desk Parcel-Id :  ' + sourceKMap.getParcelId() + ' -> ' + destKMap.getParcelId());
  destKMap.setTime(destTime);
}


function startVideo(){
  if (sync_flag == 1){
    kmap1.startVideo();
    kmap2.startVideo();
  }
}

function stopVideo(){
  if (sync_flag == 1){
    kmap1.stopVideo();
    kmap2.stopVideo();
  }
}
