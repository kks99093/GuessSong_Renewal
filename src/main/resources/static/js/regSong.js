/**
 * 
 */
 let trNumber = 0;
 
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
	