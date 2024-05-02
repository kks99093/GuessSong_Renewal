/**
 * 
 */
var roomNumber = 0;
const regTypeUserName = /^[가-힣a-zA-z\s0-9]{1,6}$/;
$(document).ready(function(){
	
	$('#closePopup').click(function(){
		const popup = document.querySelector('#popup');
		popup.classList.add('hide');
		$('#pop_password_div').remove();
		$('#name').val('');
		roomNumber = 0;
	})
	
	//팝업
	$('.gameList_tr').click(function(){
		const popup = document.querySelector('#popup');
		popup.classList.add('has-filter');
		popup.classList.remove('hide');
		roomNumber = $(this).attr("param1");
		let passwordChkInput = $(this).attr("param2");
		if(passwordChkInput == 1){
			$('.pop_input_div').append('<div id="pop_password_div"><span>비밀번호 : </span><input type="password" id="password"><div>')
		}
	})
	
	
	$('#playGame').click(function(){
		let name = $('input[name=name]').val();
		userName = name.trim();
		
		if((name == null || name == '' || !regTypeUserName.test(name)) && ($('#loginName').length < 1)) {
			alert('닉네임을 1~6글자로 입력해 주세요')
			return ;
		}else{
			let password = $('#password').val();
			
			let data = {
				"userInfo" :{
					name : name
				},
				"gameRoom" :{
					roomPk : roomNumber,
					password : password
				}
			}
			
			$.ajax({
				type: "POST",
				url : "/rest/userNameChk",
				data : JSON.stringify(data),
				contentType : "application/json; charset=utf-8",
				dataType: "json"
			}).done(function(resp){
				if(resp == 0){
					alert('현재 방에 동일한 이름을 사용하는 사람이 있습니다')
					return
				}else if(resp == -1){
					alert('비밀번호가 틀렸습니다').
					return;
				}else if(resp == -2){
					alert('인원이 가득 찼습니다.')
					return;
				}else{
					var form = document.createElement('form');
					form.setAttribute('method', 'post');
				    form.setAttribute('action', '/board/gameBoard');
					document.charset = 'URF-8';
					var params = {
							roomPk : roomNumber,
							name : name,
					}
					
					for(var key in params){
						var hiddenField = document.createElement('input');
					      hiddenField.setAttribute('type', 'hidden');
					      hiddenField.setAttribute('name', key);
					      hiddenField.setAttribute('value', params[key]);
					      form.appendChild(hiddenField);
					}
					
					document.body.appendChild(form);
					form.submit();
				}
			})
		}
		
		
	})

})


function pageMove(pageNumber){
	location.href = "/board/gameList?page="+pageNumber;
}
	