/**
 * 
 */

const regTypeUserName = /^[가-힣a-zA-z\s0-9]{1,6}$/;
const regTypeTitle = /^.{3,30}$/;
 
 $(document).ready(function(){
	 let loginFaileChk = $('#loginFaileChk').val();
	 let joinResult = $('#joinResult').val();
	 if(loginFaileChk != ''){
		let loginPop = document.querySelector('#loginPop');
		loginPop.classList.add('has-filter');
		loginPop.classList.remove('hide');
		alert("아이디 비밀번호를 확인해 주세요");		
	 }
	 console.log(joinResult);
	 if(joinResult != ''){
		 if(joinResult == 1){
			 alert("회원가입이 완료되었습니다.");
		 }else{
			 alert("회원가입에 실패하였습니다.");
		 }
	 }

	 
	 for(let i = 2024; i >= 1990; i--){		 
		 $('#beforeYears').append(`<option value="`+i+`">` + i + `</option>`);
		 $('#afterYears').append(`<option value="`+i+`">` + i + `</option>`);
	 }
		
	$('#logoinBtn').click(() =>{
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
	
	$('#joinBtn').click(() =>{
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
		
	
	//방 만들기

		
		
	
	$('#create_btn').click(async function(){
		let userName = $('#userName').val();
		userName = userName.trim();
		let title = $('#title').val();
		title = title.trim();
		let password = $('#password').val();
		password = password.trim();
		

		$('#createRoomFrm').attr('action', '/board/gameBoard')
		if(await songInfoChk() == -1){
			alert("해당되는 년도와 장르의 노래가 충분하지 않습니다.")
			return ;	
		}else if(userName == null || userName == '' || !regTypeUserName.test(userName)){
			alert('닉네임을 1~6글자로 입력해 주세요')
			return ;
		}else if(title == null || title == '' || !regTypeTitle.test(title)){
			alert('방제목을 3~30글자로 입력해 주세요')
			return ;
		}else{
			$('#createRoomFrm').submit();
		}
			
	})
		
	
	
	async function songInfoChk(){
		return new Promise(resolve =>{
			let category = $('#category').val();
			let count = $('#count').val();
			let beforeYears = $('#beforeYears').val(); 
			let afterYears = $('#afterYears').val();
			data ={
				category,
				count,
				beforeYears,
				afterYears
			}
			$.ajax({
				type: "POST",
				url : "/rest/songInfoChk",
				data : JSON.stringify(data),
				contentType : "application/json; charset=utf-8",
				dataType: "json"
			}).done(function(resp){
				resolve(resp);
			})
		})
		
		
		
		
	}	

		
		
		
		
})
	
	
