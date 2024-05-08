/**
 * 
 */
const regTypeJoinUserName = /^[a-z]+[a-zA-Z0-9]{5,19}$/
const regTypeJoinName = /^[가-힣a-zA-z\s0-9]{1,6}$/;
 
 $(document).ready(function(){
	 let loginFaileChk = $('#loginFaileChk').val();
	 if(loginFaileChk != ''){
		let loginPop = document.querySelector('#loginPop');
		loginPop.classList.add('has-filter');
		loginPop.classList.remove('hide');
		alert("아이디 비밀번호를 확인해 주세요");		
	 }

	$('#logoinPopBtn').click(() =>{
		let loginPop = document.querySelector('#loginPop');
		loginPop.classList.add('has-filter');
		loginPop.classList.remove('hide');
	})

	$('#loginCloseBtn').click(()=>{
		let loginPop = document.querySelector('#loginPop');
		loginPop.classList.remove('has-filter');
		loginPop.classList.add('hide');
		$('#loginId').val('');
		$('#loginPw').val('');
		
	})
	
	$('#joinPopBtn').click(() =>{
		let joinPop = document.querySelector('#joinPop');
		joinPop.classList.add('has-filter');
		joinPop.classList.remove('hide');
	}) 
	
	

	
	$('#joinCloseBtn').click(() =>{
		let joinPop = document.querySelector('#joinPop');
		joinPop.classList.remove('has-filter');
		joinPop.classList.add('hide');
		$('#joinId').val('');
		$('#joinPw').val('');
		$('#joinName').val('');
	})

	$('#logoutBtn').click(() => {
		location.href = "/logout";
	})
	
	$('#regSong').click(()=>{
		location.href = "/admin/regSong";
	})
	
	$('#gameListBtn').click(()=>{
		location.href = "/board/gameList";
	})
	
	$('#gameCreateBtn').click(()=>{
		location.href = "/board/main";
	})
	
	$('#updSong').click(()=>{
		location.href = "/admin/updSong";
	})
	
	
	$('#joinBtn').click(async () =>{
		
		let username = $('#joinId').val();
		let password = $('#joinPw').val();
		let name = $('#joinName').val();
		username = username.trim();
		name = name.trim();
		if(!regTypeJoinUserName.test(username)){
			alert('아이디는 영어 + 숫자 , 5~19 글자로 입력해주세요')
			return;
		}else if(!regTypeJoinUserName.test(password)){
			alert('비밀번호는 영어 + 숫자 5~19글자로 입력해주세요')
			return;
		}else if(!regTypeJoinName.test(name)){
			alert('닉네임은 한글, 영어, 숫자 1~6글자 사이로 입력해주세요')
			return;
		}
		
		let result = await dupliChk(username, name);
		
		if(result == -1){
			alert("이미 존재하는 아이디 입니다.")
			return
		}else if(result == -2){
			alert("이미 존재하는 닉네임 입니다.")
			return
		}else {
			console.log('good')
			$('#joinFrm').submit();
		}
		
		
	})
		
	function dupliChk(username, name){		
		return new Promise(resolve => {
			data ={
				username,
				name
			}		
			$.ajax({
				type : "POST",
				url : "/rest/dupliChk",
				data : JSON.stringify(data),
				contentType : "application/json; charset=utf-8",
				dataType : "json"				
			}).done(function(resp){
				resolve(resp);
			})
		})				
		
		
	}

		
		
		
})
	
	
