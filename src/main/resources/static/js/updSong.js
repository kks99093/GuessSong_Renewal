/**
 * 
 */
 
$(document).ready(function(){
	
	
})

function updSong(thisBtn){	
	let songPk = thisBtn.value;
	thisBtn.innerHTML = '완료';
	thisBtn.setAttribute("onclick", "updSongConFirm("+songPk+")")
	let className = 'input'+songPk;
	$('.'+className+'').removeClass('readonly');
}

function delSong(thisBtn){	
	if(confirm("정말 삭제 하시겠습니까?")){
		let songPk = thisBtn.value;	
		let data = {
			songPk
		}
		$.ajax({
			type : "POST",
			url : "/rest/delSong",
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8"
			
		}).done(function(resp){
			if(resp == 1){
				alert('정상적으로 삭제 되었습니다.');
				location.reload();
			}else{
				alert('삭제에 실패했습니다 ㅠ');
			}
		})
	}
	return;
	
	
	
	
}

function updSongConFirm(songPk){
	let youtubeUrl = $('#youtubeUrl'+songPk).val();
	let answer = $('#answer'+songPk).val();
	let hint = $('#hint'+songPk).val();
	let year = $('#year'+songPk).val();
	let category = $('#category'+songPk+'').val();
	
	
	if(year > 2024 || year < 1990){
		alert("1990년 ~ 2024년 사이의 년도만 입력 가능합니다.")
		return 
	}else{
		let data ={
			songPk,
			youtubeUrl,
			answer,
			hint,
			year,
			category
		}
		
		$.ajax({
			type : "POST",
			url : "/rest/updSong",
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8"
		}).done(function(resp) {
			if(resp == -1){
				alert("유튜브 주소를 입력해 주세요");
				return
			}else{
				alert("노래를 업데이트 하였습니다.");
				let thisBtn = $('#updSongBtn'+songPk);
				thisBtn.attr("onclick", "updSong(this)")
				thisBtn.html("수정");
				$('#youtubeUrl'+songPk).val(resp);
				let className = 'input'+songPk;
				$('.'+className+'').addClass('readonly');											
			}
		})
	}
	


	
}