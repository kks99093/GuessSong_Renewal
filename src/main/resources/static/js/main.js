/**
 * 
 */
 
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
	
		

		
		
		
})
	
	
