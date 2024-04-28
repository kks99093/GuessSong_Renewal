/**
 * 
 */
let modeSel = 2;
const regTypeUserName = /^[가-힣a-zA-z\s0-9]{1,6}$/;
const regTypeTitle = /^.{3,30}$/;
 $(document).ready(function(){
	
		
		$('#create_btn').click(function(){
			let userName = $('#userName').val();
			userName = userName.trim();
			let title = $('#title').val();
			title = title.trim();
			let password = $('#password').val();
			password = password.trim();
			if(modeSel == 1){
				$('#createRoomFrm').attr('action', '/board/soloGameBoard')
				if(userName == null || userName == '' || !regTypeUserName.test(userName)){
					alert('닉네임을 1~6글자로 입력해 주세요')
					return;
				}else{
					$('#createRoomFrm').submit();
				}				
			}else if(modeSel == 2){
				$('#createRoomFrm').attr('action', '/board/multiGameBoard')
				if(userName == null || userName == '' || !regTypeUserName.test(userName)){
					alert('닉네임을 1~6글자로 입력해 주세요')
					return ;
				}else if(title == null || title == '' || !regTypeTitle.test(title)){
					alert('방제목을 3~30글자로 입력해 주세요')
					return ;
				}else{
					$('#createRoomFrm').submit();
				}
			}else{
				alert('모드를 선택해 주세요')
				return;
			}
			
		})
		
		
		
		
		
		
		
		
		
		
		
	})
	