<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<!-- SQL 정답 입력 화면 DIV -->
	<div class="sqlAnswer">
		<div class="sqlAnswersheet">
			<textarea style="width:480px;float:left;margin-right:-480px;font-family:monospace;white-space:pre;overflow:hidden;" disabled="disabled" class="lined"></textarea>
			<textarea style="font-family:monospace;width:480px;float:right;" cols="149" rows="10" id="sql" onkeyup="getLineNumberAndColumnIndex(this);" onmouseup="this.onkeyup();">SELECT
   *
FROM
   ANIMAL;</textarea><br>
			<div align="right">
				<button id="sqltest" onclick="javascript:sqlrun();" style="text-align: right;">Submit</button><br>
			</div>
			<div>
				<span>Console</span>	
				<textarea cols="149" rows="5" id="resultView"></textarea> 
			</div>
		</div>
	</div>
	<!-- SQL 정답 입력 화면 DIV 종료 -->