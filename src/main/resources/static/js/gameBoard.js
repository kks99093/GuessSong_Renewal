/**
 * 
 */

var ws;
var userName = $('#name').val();
let roomNumber = $('#roomNumber').val();
let songBoardPk = $('#songBoardPk').val();
var youtubeUrl = "";
var player;
let answerReady = '0';
let gameStartChk = 0;
let totalSongNum = 0;
var nextSongTimer;
let songNumber= 1;
$(document).ready(function(){

	//넘기기
	$('#skip_div').click(()=>{
		skipSongBtn()
	})
	
	$('#result_div').click(()=>{
		resultSongBtn();
	})
	
	$('#chat_submit').click(()=>{
		send();
	})
	
	$('#result_home_btn').click(()=>{
		location.href = '/board/main';
	})
	
	$('#soundVolumeInput').on('keyup', function(e) {
	    this.value = this.value.replace(/\D/g, '');
	    if(this.value > 100){
			this.value = 100;
		}else if(this.value < 0){
			this.value = 0;
		}
	    
		if(e.keyCode == 13){
			let soundVolume = $('#soundVolumeInput').val();
			player.setVolume(soundVolume);
		}
	});
	
	
	$('#soundVolumeBtn').click(()=>{
		let soundVolume = $('#soundVolumeInput').val()
		player.setVolume(soundVolume);
	})


})


function wsOpen(){
	ws = new WebSocket("ws://" + location.host + "/chating/"+roomNumber+"/"+userName);	
	wsEvt();
}
	
function wsEvt() {
	ws.onopen = function(data){
		//소켓이 열리면 초기화 세팅하기
	}
	
	ws.onmessage = function(data) {
		var msg = data.data;
		if(msg != null && msg.trim() != ''){
			var jsonObject = JSON.parse(msg);
			//ㅡㅡㅡㅡㅡㅡㅡ처음 접속시 id저장
			switch(jsonObject.type){
				case 'sessionId':
					addSessionIdType(jsonObject);
					break;
				case 'join':
					joinUserType(jsonObject);
					break;
				case 'left':
					leftUserType(jsonObject);
					break;
				case 'message':
					receiveMessageType(jsonObject)
					break;
				case 'gameStart':
					gameStartType(jsonObject);
					break;
				case 'skipSong':
					skipSong(jsonObject);
					break;
				case 'ready':
					receiveReady(jsonObject);
					break;
				case 'readyCencel' :
					receiveReadyCencel(jsonObject);
					break;
				case 'nextSongChk' :
					nextSongChk(jsonObject);
					break;
				case 'resultSong' :
					resultSong(jsonObject);
					break;
			}
			
		}
	}
	
	$('#chatInput').keypress((e)=>{
		if(e.keyCode == 13){ //enter press
			send();
		}
	})
}



function send() {
	var payload = {
			type : 'message',
			msg : $('#chatInput').val(),
			sessionId: $('#sessionId').val(),
			roomNumber : $('#roomNumber').val(),
			answerReady : answerReady
	}	
	ws.send(JSON.stringify(payload));
	$('#chatInput').val("");
}

wsOpen()

//ㅡㅡㅡㅡㅡㅡ소켓관련 끝ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


//소켓 메세지 type별 처리
function addSessionIdType(jsonObject){
	$('#sessionId').val(jsonObject.sessionId)
	$('#color').val(jsonObject.color)
	$('.gameBoard_userInfo').append('<div class="userInfo_div" id="'+jsonObject.sessionId+'_div"> <div class="userName border"><span class="'+jsonObject.color+'">'+ userName +'</span></div> <div class="userPoint border"><span class="score_span" id="'+jsonObject.sessionId+'_score">0</span></div> </div>')
	$('#currentSong').html('0');
	$('#totalSong').html(jsonObject.totalSongNum);
	totalSongNum = jsonObject.totalSongNum;
	if(jsonObject.reader == jsonObject.sessionId){
		$('.gameBoard_songInfo').prepend('<div class="startGame_div" id="startGame_div" onclick="startGame()"><span id="startGame_span" >시작하기</span></div>');
		$('.reader_mark').remove();
		$('#'+jsonObject.reader+'_div').append('<div class="reader_mark">방장</div>')
	}else{
		$('.gameBoard_songInfo').prepend('<div class="startGame_div" id="readyGame_div" onclick="ready()"><span id="readyGame_span">레디</span></div>')
	}
	youtubeUrl = jsonObject.youtubeUrl;
	var tag = document.createElement('script');
	tag.src = "https://www.youtube.com/iframe_api";
	var firstScriptTag = document.getElementsByTagName('script')[0];
	firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
	
}

function joinUserType(jsonObject){
	if(jsonObject.user != null){
		//입장한 사람의 정보를 추가
		$('.gameBoard_userInfo').append('<div class="userInfo_div" id="'+jsonObject.user.sessionId+'_div"> <div class="userName"><span class="'+jsonObject.user.color+'">'+ jsonObject.user.userName +'</span></div> <div class="userPoint"><span class="score_span" id="'+jsonObject.user.sessionId+'_score">0</span></div> </div>')
		$("#chatData").append('<p class="chatData alertMsg "><span class="'+jsonObject.color+'">' + jsonObject.user.userName + '</span> 님이 방에 입장하셨습니다.</p>');
	}else{
		//다른사람정보를 추가
		for(i = 0; i < jsonObject.userList.length; i++){
			$('.gameBoard_userInfo').append('<div class="userInfo_div" id="'+jsonObject.userList[i].sessionId+'_div"> <div class="userName"><span class="'+jsonObject.userList[i].color+'">'+ jsonObject.userList[i].userName +'</span></div> <div class="userPoint"><span class="score_span" id="'+jsonObject.userList[i].sessionId+'_score">0</span></div></div>')
			$('.reader_mark').remove();
			$('#'+jsonObject.reader+'_div').append('<div class="reader_mark">방장</div>')
			if(jsonObject.userList[i].ready == 1){
				$('#'+jsonObject.userList[i].sessionId+'_div').append('<div class="ready_div" id="'+jsonObject.userList[i].sessionId+'_ready_div">READY</div>')
			}
			
		}
		
	}
}

function leftUserType(jsonObject){
	$("#chatData").append('<p class="chatData alertMsg"><span class="'+jsonObject.color+'">' + jsonObject.leftUser + '</span> 님이 방에서 나가셨습니다.</p>');
	$('#'+jsonObject.sessionId+'_div').remove();
	if(jsonObject.reader != null){
		$('#'+jsonObject.reader+'_div').append('<div class="reader_mark">방장</div>')
		let mySessionId = $('#sessionId').val();
		$('.ready_div').remove();
		if(mySessionId == jsonObject.reader && gameStartChk == 0){
			$('#readyGame_div').remove();
			$('#readyCancel_div').remove();
			$('.gameBoard_songInfo').prepend('<div class="startGame_div" id="startGame_div" onclick="startGame()"><span id="startGame_span" >시작하기</span></div>');
		}
	}
	 
	}

function receiveMessageType(jsonObject){
	if(jsonObject.answerChk == 1){ //정답일 경우
		answerReady = '0';
		$("#chatData").append('<p class="answerMsg"> 정답 - <span class="'+jsonObject.color+'">'+jsonObject.userName+'</span> : ' + jsonObject.msg + "</p>");
		$('#'+jsonObject.sessionId+'_score').html(''+jsonObject.score+'');
		$('#before_songList').append('<p><span>'+songNumber +'. '+ jsonObject.beforeAnswer+'</span></p>');
		songNumber++
		//10초후 다음노래
		if(jsonObject.youtubeUrl == "" || jsonObject.youtubeUrl == null || jsonObject.youtubeUrl == undefined){
			$('.gameBoard_songInfo').prepend('<div class="loading_div" id="loading_div"><span class="loading_span">곧 게임이 종료됩니다. !!!</span></div>');
			nextSongTimer = setTimeout(()=>{
				let payload = {
					type : 'resultSong',
					roomNumber : $('#roomNumber').val(),
					answerToEnd : true
				}
				ws.send(JSON.stringify(payload));
				
			},10000)	
		}else{
			youtubeUrl = jsonObject.youtubeUrl;	
			$('.gameBoard_songInfo').prepend('<div class="loading_div" id="loading_nextSong_div"><span class="loading_span">잠시 후 다음 노래로 넘어갑니다.</span></div>');
			nextSongTimer = setTimeout(()=>{
				nextSong();
			},10000)	
		}
		
	}else{ //정답이 아닐 경우
		let sessionId = $('#sessionId').val();
		if(jsonObject.sessionId == sessionId){
			$("#chatData").append('<p class="my_chat chatData"><span class="'+jsonObject.color+'">' +jsonObject.userName + '</span> : ' + jsonObject.msg + '</p>');
		}else{
			$("#chatData").append('<p class="chatData"><span class="'+jsonObject.color+'">' +jsonObject.userName + '</span> : ' + jsonObject.msg + '</p>');			
		}
	}	
	
	$('#chatData').scrollTop($('#chatData')[0].scrollHeight);
	$('#before_songList').scrollTop($('#before_songList')[0].scrollHeight);
}



function skipSong(jsonObject){
	if(jsonObject.skipChk == 1){
		clearTimeout(nextSongTimer)
		youtubeUrl = jsonObject.youtubeUrl;
		$('#skipCount_div').css('display','none')
		$('#skip_count_span').html('');
		if(answerReady == '1'){
			$('#before_songList').append('<p><span>'+songNumber +'. '+ jsonObject.beforeAnswer+'</span></p>');
			$('#before_songList').scrollTop($('#before_songList')[0].scrollHeight)
			songNumber++
			answerReady = '0';
		}
		
		nextSong();
	}else if(jsonObject.skipChk == 0){
		$('#skip_count_span').html(" : " + jsonObject.skipCount);
	}
}

//ㅡㅡㅡㅡㅡ소켓 메세지 type별 처리 끝


//ㅡㅡㅡ 유튜브 iframe apiㅡㅡㅡㅡ
function youtubePlay(){
	var tag = document.createElement('script');
	tag.src = "https://www.youtube.com/iframe_api";
	var firstScriptTag = document.getElementsByTagName('script')[0];
	firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
}

	
function onYouTubeIframeAPIReady() {
  	player = new YT.Player('player', {
	    height: '300',
	    width: '300',
	    videoId: youtubeUrl, //여기에 비디오 ID를 삽입한다. 
		//만약에 유튜브 공유 주소가 https://www.youtube.com/watch?v=Wac9LIURW1I라면 v=뒤의 값을 넣는다
		events:{
			'onStateChange' : onPlayerStateChange
		}
	});
	
}
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ



function ready(){
	$('#readyGame_div').remove();
	$('.gameBoard_songInfo').prepend('<div class="startGame_div disable_evt disable_cursor" id="readyCancel_div" onclick="readyCencel()"><span id="readyCancel_span">레디 취소</span></div>')
	setTimeout(()=>{
		$('#readyCancel_div').removeClass('disable_evt');
		$('#readyCancel_div').removeClass('disable_cursor');
	}, 2000);
	var payload = {
			type : 'ready',
			roomNumber : $('#roomNumber').val(),
	}
	ws.send(JSON.stringify(payload));
}

function receiveReady(jsonObject){
	$('#'+jsonObject.sessionId+'_div').append('<div class="ready_div" id="'+jsonObject.sessionId+'_ready_div">READY</div>')
}

function readyCencel(){
	$('#readyCancel_div').remove();
	$('.gameBoard_songInfo').prepend('<div class="startGame_div disable_evt disable_cursor" id="readyGame_div" onclick="ready()"><span id="readyGame_span">레디</span></div>')
	setTimeout(()=>{
		$('#readyGame_div').removeClass('disable_evt');
		$('#readyGame_div').removeClass('disable_cursor');
	}, 2000);
	
		var payload = {
			type : 'readyCencel',
			roomNumber : $('#roomNumber').val()
	}
	ws.send(JSON.stringify(payload));
}

function receiveReadyCencel(jsonObject){
	$('#'+jsonObject.sessionId+'_ready_div').remove();
	
}

//게임 시작
function startGame(){
	$('#startGame_div').remove();
	var payload = {
				type : 'gameStart',
				roomNumber : $('#roomNumber').val() 
		}
	ws.send(JSON.stringify(payload));
	
}

function gameStartType(jsonObject){
	answerReady = '0';
	if(jsonObject.readyChk == 1){
		$('#readyCancel_div').remove();
		$('#ready_div').remove();
		$('.ready_div').remove();
		$('.gameBoard_songInfo').prepend('<div class="loading_div" id="loading_div"><span class="loading_span">곧 게임이 시작됩니다. !!!</span></div>');
		gameStartChk = 1;
		setTimeout(()=>{
			$('#currentSong').html('1');
			$('#loading_div').remove();
			$('#skip_div').css('display', 'flex');
			answerReady = '1';
			let soundVolume = player.getVolume();
			$('#soundVolumeInput').attr('readonly',false);
			$('#soundVolumeInput').val(soundVolume);
			player.unMute();
			player.playVideo();
		},3000)		
	}else{
		alert('레디하지 않은 인원이 있습니다.')
		$('.gameBoard_songInfo').prepend('<div class="startGame_div" id="startGame_div" onclick="startGame()"><span id="startGame_span" >시작하기</span></div>');
	}

}


//다음노래 메세지 소켓 전송
function skipSongBtn(){
	var payload = {
			type : 'skipSong',
			roomNumber : $('#roomNumber').val() 
	}
	$('#skip_div').addClass('disable_evt', 'disable_cursor');
	ws.send(JSON.stringify(payload));
}


// 얘를 없앨 생각
function nextSongChk(jsonObject){
	if(jsonObject.nextSongChk == 1){
		$('#skip_div').css('display', 'none');
		$('#skip_div').removeClass('disable_evt', 'disable_cursor');
		$('#loading_nextSong_div').remove();
		$('.gameBoard_songInfo').prepend('<div class="loading_div" id="loading_div"><span class="loading_span">곧 노래가 시작됩니다. !!!</span></div>');
		setTimeout(()=>{
			if(totalSongNum == jsonObject.currentSong+1){
				$('#result_div').css('display', 'flex');
				//result_div에 결과창 띄우는 함수 추가
			}else{
				$('#skip_div').css('display', 'flex');	
			}
			$('#currentSong').html(jsonObject.currentSong+1);
			$('#loading_div').remove();
			
			answerReady = '1';
			player.unMute();
			player.playVideo();
		},3000)	
	}
}

function delayTime(){
	let sleep = 0;
	let color = $('#color').val();
	let colorList = ["red", "blue", "green", "gray", "black", "brown", "purple", "yellow"];
	
	for(i = 0; i < colorList.length; i++){
		if(color == colorList[i]){
			sleep = i * 50;
			break;
		}
	}
	return sleep;
}


function nextSong(){
	player.stopVideo()
	player.clearVideo()
	$('#player').remove();
	$('#youtubePlayer').append('<div id="player"></div>')
	onYouTubeIframeAPIReady()
	let sleep = delayTime();
	setTimeout(()=>{
		var payload = {
			type : 'nextSongChk',
			roomNumber : $('#roomNumber').val() 
		}
		ws.send(JSON.stringify(payload));
	}, sleep)
}





function onPlayerStateChange(event){
	if(event.data == 0){
		player.playVideo();
	}
}

function resultSongBtn(){
	let payload = {
		type : 'resultSong',
		roomNumber : $('#roomNumber').val()
	}
	$('#result_div').addClass('disable_evt', 'disable_cursor');
	ws.send(JSON.stringify(payload));
}

function resultSong(jsonObject){
	if(jsonObject.resultChk == 1){
		console.log("dd")
		gameEnd(jsonObject);
	}else{
		console.log("ss")
		$('#result_count_span').html(" : " + jsonObject.resultCount);
	}
}

function gameEnd(jsonObject){
	clearTimeout(nextSongTimer)
	if(answerReady == '1'){
		$('#before_songList').append('<p><span>'+songNumber +'. '+ jsonObject.beforeAnswer+'</span></p>');
		$('#before_songList').scrollTop($('#before_songList')[0].scrollHeight)
		songNumber++
		answerReady = '0';
		$('.gameBoard_songInfo').prepend('<div class="loading_div" id="loading_div"><span class="loading_span">곧 게임이 종료됩니다. !!!</span></div>');
	}
	$('#result_div').css('display', 'none');
	setTimeout(()=>{
			for(i = 0; i < jsonObject.userList.length; i++){
			for(j = i+1; j <jsonObject.userList.length; j++){
				if(jsonObject.userList[j].score > jsonObject.userList[i].score){
					let userListTemp = jsonObject.userList[i];
					jsonObject.userList[i] = jsonObject.userList[j];
					jsonObject.userList[j] = userListTemp; 
				}	
			}
		}
		
		$('.gameBoard_div').remove();
		
		for(i = 0 ; i < jsonObject.userList.length; i++){
			let rank = i+1;
			$('.result_table_tbody').append('<tr><td class="result_table_td num_td" >'+rank+'</td>  <td class="result_table_td name_td '+jsonObject.userList[i].color+'" >'+jsonObject.userList[i].userName+'</td>  <td class="result_table_td score_td" >'+jsonObject.userList[i].score+'</td></tr>')		
		}
		
		$('.result_table_div').css('display', 'flex')
	
	}, 2000)
	
}

function playerVolume(volumeNum){
	player.setVolume(volumeNum)
}