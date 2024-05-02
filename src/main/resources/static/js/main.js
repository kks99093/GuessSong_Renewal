/**
 * 
 */

const regTypeName = /^[가-힣a-zA-z\s0-9]{1,6}$/;
const regTypeTitle = /^.{3,30}$/;
 
 $(document).ready(function(){
	 
	 for(let i = 2024; i >= 1990; i--){		 
		 $('#beforeYears').append(`<option value="`+i+`">` + i + `</option>`);
		 $('#afterYears').append(`<option value="`+i+`">` + i + `</option>`);
	 }
		
	//방 만들기
	$('#create_btn').click(async function(){
		let name = $('#name').val();
		name = name.trim();
		let title = $('#title').val();
		title = title.trim();
		let password = $('#password').val();
		password = password.trim();
		

		$('#createRoomFrm').attr('action', '/board/gameBoard')
		if(await songInfoChk() == -1){
			alert("해당되는 년도와 장르의 노래가 충분하지 않습니다.")
			return ;	
		}else if((name == null || name == '' || !regTypeName.test(name)) && ($('#loginName').length < 1) ){
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
	
	
