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
			$('#songList_tbody').append('<tr id="songInfoTr'+trNumber+'">'+
				'<td><input type="text" placeholder="유튜브 주소" name="youtubeUrl" class="youtubeUrl"></td> '+
				'<td><input type="text" placeholder="정답" name="answer" class="answer"> </td>'+
				'<td><input type="text" placeholder="힌트" name="hint" class="hint"></td>'+
				'<td><input type="number" placeholder="년도" name="year" id="year" class="hint"></td>'+
				'<td>'+
					'<select name="category">'+
						'<option value="ballad"> 발라드 </option>'+
						'<option value="idol"> 아이돌 </option>'+
						'<option value="hiphop"> 힙합 </option>'+
						'<option value="pop"> 팝송 </option>'+
						'<option value="R&B"> R&B </option>'+
					'</select>'+
				'</td>'+
				'<td><button class="remove_btn" type="button" onclick="removeTr('+trNumber+')">X</button></td></tr>');
			
		})
		
		
	})
	
	function removeTr(trNumberParam){
		
		$('#songInfoTr'+trNumberParam).remove();
	}
	