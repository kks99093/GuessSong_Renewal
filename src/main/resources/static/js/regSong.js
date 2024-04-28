/**
 * 
 */
 let trNumber = 0;
 const regTypeTitle = /^.{3,30}$/;
 const regTypePw = /^[a-z0-9]{3,6}$/;

	$(document).ready(function(){
		
		$('.submit_btn').click(()=>{
			
		})
		
		
		$('#add_songList_btn').click(function(){
			if(trNumber == 0){
				trNumber = $('#trNumber').val();
			}
			trNumber++
			$('#songList_tbody').append('<tr id="songInfoTr'+trNumber+'"><td><input type="text" placeholder="유튜브 주소" name="youtubeUrl" class="youtubeUrl"></td> <td><input type="text" placeholder="정답" name="answer" class="answer"> </td> <td><input type="text" placeholder="힌트" name="hint" class="hint"></td><td><button class="remove_btn" type="button" onclick="removeTr('+trNumber+')">X</button></td></tr>');
		})
		
		
	})
	
	function removeTr(trNumberParam){
		
		$('#songInfoTr'+trNumberParam).remove();
	}
	
	function submitChk(){
		let title = $('#title').val();
		title = title.trim();
		let password = $('#password').val();
		password = password.trim();
		if(title == null || title == "" || !regTypeTitle.test(title)){
			alert('제목은 3~30글자를 입력해 주세요')
			return false;
		}else if(password == null || password == "" || !regTypePw.test(password)){
			alert('비밀번호는 영어(소문자),숫자로 3~6글자를 입력해 주세요')
			return false;
		}else{
			return true;	
		}
		
	}
