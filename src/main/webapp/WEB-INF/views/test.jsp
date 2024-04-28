<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="/js/jquery-3.6.0.min.js"></script>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div id="time"></div>
<button id="clickButton" onclick="youtubePlay()">만들기</button><br>
<button id="clickButton" onclick="playVideo()">시작하기</button><br>
<button id="clickButton" onclick="nextVideo('HL_N7-gGEL8')">다음</button><br>
<div id="youtubePlayer">
	<div id="player"></div>
</div>
<script>
var player;
var time = 4;
function printTime(){
	$('#time').html(time + '초 후 시작합니다')
}

function youtubePlay(){
	
	var count = setInterval(function(){
		time--
		printTime()		
		if(time == 0){
			clearInterval(count)
			$('#time').html('')
		}
	}, 1000)
	
	setTimeout(function(){
		var tag = document.createElement('script');
		tag.src = "https://www.youtube.com/iframe_api";
		var firstScriptTag = document.getElementsByTagName('script')[0];
		firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
	}, 4000)

}

function onYouTubeIframeAPIReady(videoIdParam) {
	if(videoIdParam == null){
		videoIdParam = 'Wac9LIURW1I'
	}
  	player = new YT.Player('player', {
    height: '300',
    width: '300',
    videoId: videoIdParam, //여기에 비디오 ID를 삽입한다. 
    playerVars: { 
    	'autoplay': 1,
    	start : 10,
    	end : 15
    	}
  	,events : {
  		'onStateChange': onPlayerStateChange // 구간반복을 위해 추가해줌
  	}
//만약에 유튜브 공유 주소가 https://www.youtube.com/watch?v=Wac9LIURW1I라면 v=뒤의 값을 넣는다
  });
  
}

function checkPlayerState() {
	  alert(player.getPlayerState());
	  alert(player.getCurrentTime());
	}
	
function playVideo(){
	player.playVideo()
}

function nextVideo(videoIdParam){
	player.stopVideo()
	player.clearVideo()
	$('#player').remove();
	$('#youtubePlayer').append('<div id="player"></div>')
	onYouTubeIframeAPIReady(videoIdParam)
}

//구간 반복 재생
function onPlayerStateChange(event) {
	if (event.data == YT.PlayerState.ENDED) {
		player.seekTo(10); //여기에 시작시간을 넣어주면 되네
	}
}

</script>
</body>
</html>