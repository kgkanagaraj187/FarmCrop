<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<head>
	<meta name="decorator" content="swithlayout">
</head>
<style>
.ui-jqgrid .ui-jqgrid-htable th div {
	height: auto;
	overflow: hidden;
	padding-right: 4px;
	position: relative;
	white-space: normal !important;
}

th.ui-th-column div {
	white-space: normal !important;
	height: auto !important;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	/*height: auto !important;*/
}
</style>
<body>
	<script type="text/javascript">
        var mtntReceiptNumber = '';
        var mtnrReceiptNumber = '';
        var cooperativeId = '';
        var truckId = '';
        var driverName = '';
        //var transType = '';
        var sDate = '';
        var eDate = '';
        var type='';
        var seasonId = '';
        var proCenter='';
        var ginning='';
        var fieldStaff='';
        var season='';
	    var detailAction = "<%=request.getParameter("url")%>?type=<%=request.getParameter("type")%>";    
        var listAction = "<%=request.getParameter("url1")%>?type=<%=request.getParameter("type")%>";    
        var populateAction = "<%=request.getParameter("url4")%>?type=<%=request.getParameter("type")%>";
        var tenant='<s:property value="getCurrentTenantId()"/>';
        jQuery(document).ready(function ( ) {
        	jQuery(".well").hide();
        	 var isKpfBased = '<s:property value="%{getIsKpfBased()}" />';
            type= '<%=request.getParameter("type")%>';
            onFilterData(type);
            var columnNames;
            document.getElementById('fieldl').selectedIndex = 0;
            $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
    	    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
           	$("#daterange").data().daterangepicker.updateView();
           	$("#daterange").data().daterangepicker.updateCalendars();
           	$('.applyBtn').click();
			var d1=	jQuery('#daterange').val();
							       /*  <s:if test="type == 'pmtnt'">
							                        document.getElementById('mtntReceiptNumber').value = '';
							        </s:if>
							        <s:if test="type == 'pmtnr'">
							                        document.getElementById('mtnrReceiptNumber').value = '';
							        </s:if>  */
							        
							   
		/* 	if(type!='procTrans')	{			        
            document.getElementById('cooperativeId').selectedIndex = 0;
			}
			else{
			document.getElementById('proCenter').selectedIndex = 0;
			document.getElementById('ginning').selectedIndex = 0;
			} */
			if(type=='procTrans'||type=='procRecp'){
			document.getElementById('proCenter').selectedIndex = 0;
			document.getElementById('ginning').selectedIndex = 0;
			<s:if test="currentTenantId!='livelihood'">
			document.getElementById('season').selectedIndex = 0;</s:if>
			}else{
			jQuery('#fieldStaff').selectedIndex=0;
			jQuery("#cooperativeId").selectedIndex=0;
			//jQuery("#proCenter").selectedIndex=0;
			//jQuery("#ginning").selectedIndex=0;
           
            jQuery('#driverName').val('');
            /* <s:if test="type == 'pmtnt'">
                  document.getElementById('transType').selectedIndex = 0;
            </s:if> */
			<s:if test="currentTenantId!='livelihood'">
            jQuery("#seasonId").selectedIndex=0;</s:if>
           // document.getElementById('seasonId').selectedIndex = 0;
			
            <s:if test='branchId==null'>
         		document.getElementById("branchIdParma").selectedIndex=0;
       		</s:if>
			}
			 jQuery('#truckId').val('');
            getAjaxRecord();
            function loadGrid() {
                jQuery("#detail").jqGrid({
                                        url: detailAction,
                                        pager: '#pagerForDetail',
                                        datatype: "json",
                                        mtype: 'POST',
                                        postData: {
                                        	"startDate" : function(){
                   		                     return document.getElementById("hiddenStartDate").value;           
                   		                    },		
                   		                    "endDate" : function(){
                   				               return document.getElementById("hiddenEndDate").value;
                   			                 },
		                                            /* "mtntReceiptNumber": function () {
		                                                if (type == 'pmtnt')
		                                                    return mtntReceiptNumber;
		                                                else
		                                                    return "";
		                                            },
		                                            "mtnrReceiptNumber": function () {
		                                                if (type == 'pmtnr')
		                                                    return  mtnrReceiptNumber;
		                                                else
		                                                    return "";
		                                            }, */
                                            "cooperative": function () {
                                            	 return cooperativeId!=undefined ? cooperativeId : "";
                                               
                                            },
                                            
                                            "truck": function () {
                                            	 return truckId!=undefined ? truckId : "";
                                               
                                            },
                                            "driver": function () {
                                            	 return driverName!=undefined ? driverName : "";
                                              

                                            }, 
                                           
                                            "selectedFieldStaff": function () {
                                            
                                                return fieldStaff!= undefined ? fieldStaff : "";

                                            },
                            				<s:if test="currentTenantId!='livelihood'">
                                            "season": function () {
                                            	 return seasonId!=undefined ? seasonId : "";
                                                

                                            },</s:if>
                                            /* "transType": function () {
                                                return transType;

                                            }, */
                                            <s:if test='branchId==null'>
                      		             		 "branchIdParma":function(){
                      		             			 return document.getElementById("branchIdParma").value;
                      			        		 },
                      			         </s:if>
                      			         <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
                      					  		"subBranchIdParma" : function(){
                      						 		return document.getElementById("subBranchIdParam").value;
                      					 		},
                      					  </s:if>
                                        },
                                    colNames: [
											
                                           <s:if test="isKpfBased==1">
	                                           '<s:property value="%{getLocaleProperty('report.date')}" />' ,
	                     				   	   '<s:property value="%{getLocaleProperty('report.month')}" />' ,
	                     				   	   '<s:property value="%{getLocaleProperty('report.year')}" />' ,
	                     				   </s:if> 
														
	                     				   	<s:if test="isKpfBased!=1">
   												<s:if test='branchId==null'>
													'<s:text name="%{getLocaleProperty('app.branch')}"/>',
												</s:if>
												<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
													'<s:text name="app.subBranch"/>',
												</s:if>
												<s:if test="currentTenantId!='pratibha'">  
   												'<s:text name="date"/>',
		                                        '<s:text name="seasonCode"/>',
   												'<s:text name="transferReceiptNumber"/>',
   												<s:if test="type == 'pmtnr'">
   												'<s:text name="receivedReceiptNumber"/>',
		                                        </s:if>
		                                        </s:if>
		                                        <s:if test="type == 'pmtnt'">
		                                    	'<s:property value="%{getLocaleProperty('userAgent')}" />' ,
		                                    	</s:if>
                                        	</s:if>
		                                        
                                        	<s:if test="isKpfBased==1">
                                        		'<s:property value="%{getLocaleProperty('invoiceNo')}" />' ,
                                        	</s:if>
                                        	<s:if test="type == 'pmtnt'">                                        	
                                        	'<s:property value="%{getLocaleProperty('cooperative.warehouse')}"/>',
                                        	<s:if test="currentTenantId!='pratibha'">
                                            '<s:property value="%{getLocaleProperty('senderWarehouse')}"/>', </s:if>
                                            </s:if>
                                        	<s:if test="type == 'pmtnr' && currentTenantId!='pratibha'">
                                    		'<s:property value="%{getLocaleProperty('cooperative.warehouse')}"/>',
                                        	'<s:property value="%{getLocaleProperty('senderWarehouse')}"/>',
                                        	</s:if>
                                       						// '<s:text name="receiptNo"/>',
                                        	'<s:property value="%{getLocaleProperty('truckId')}"/>',
                                        	'<s:text name="driver"/>',
                                        	<s:if test="isKpfBased==1">  
                                        		<s:if test="currentTenantId!='iffco'">  
                                        			'<s:text name="client"/>', 
                                        		</s:if>
                                        	</s:if>      
                                            <s:if test="type == 'pmtnt'">
											<s:if test="currentTenantId!='pratibha'">  
                                            '<s:property value="%{getLocaleProperty('totalBags')}" />' ,
                                            </s:if>
	                                        '<s:property value="%{getLocaleProperty('totalGrossWeight')}" />' ,
                                            </s:if>
                                            <s:if test="type == 'pmtnr'">
                                            <s:if test="currentTenantId!='pratibha'">
	                                        '<s:property value="%{getLocaleProperty('totalTransferredGrossWeight')}" />' ,   </s:if>
	                                        '<s:property value="%{getLocaleProperty('totalRecievedGrossWeight')}" />' ,	
	                                        <s:if test="currentTenantId!='pratibha'">
	                                        '<s:property value="%{getLocaleProperty('transportationLoss')}" />' ,   </s:if>
	                                        </s:if>
	                                        <s:if test="isKpfBased==1">  
	                                        <s:if test="currentTenantId!='iffco'">      
	                                        	'<s:property value="%{getLocaleProperty('totalAmt')}" />' ,                                        
		                                        '<s:property value="%{getLocaleProperty('labourCost')}" />' ,
		                                        '<s:property value="%{getLocaleProperty('transportCost')}" />' ,
                                        		'<s:property value="%{getLocaleProperty('finalAmt')}" />'
                                        		</s:if>
                                       		</s:if>
							                                       /*  <s:if test="type == 'pmtnt'">
							                                        	'<s:text name="transType"/>' 
							                                        </s:if> */
                                        ],
                                        colModel: [
                        		 	     
                       					   <s:if test="isKpfBased==1">
												{name:'procurementDate',index:'procurementDate',width:150,sortable:false},
												{name:'procurementMonth',index:'procurementMonth',width:150,sortable:false},
												{name:'procurementYear',index:'procurementYear',width:150,sortable:false},
										   </s:if>
												<s:if test="isKpfBased!=1">
											<s:if test='branchId==null'>
                   					   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
                   					   			value: '<s:property value="parentBranchFilterText"/>',
                   					   			dataEvents: [ 
                   					   			          {
                   					   			            type: "change",
                   					   			            fn: function () {
                   					   			            	console.log($(this).val());
                   					   			             	getSubBranchValues($(this).val())
                   					   			            }
                   					   			        }]
                   					   			
                   					   			}},	   				   		
                   					   </s:if>
                   					   <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
                   					   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
                  					   </s:if>  
                   					   		<s:if test="currentTenantId!='pratibha'"> 
	                                            {name: 'mtntDate', index: 'mtntDate',width:80,sortable: false},
	                                            {name: 'seasonCode', index: 'seasonCode',sortable: false},
	                                            {name: 'transferReceiptNumber', index: 'transferReceiptNumber',width:80,sortable: false},
	                                        	<s:if test="type == 'pmtnr'">
	                                            {name: 'recievedReceiptNumber', index: 'recievedReceiptNumber',width:80,sortable: false},
	                                            </s:if>
	                                            </s:if>
	                                            <s:if test="type == 'pmtnt'">
	                                            {name: 'agentId', index: 'agentId',sortable: false},
	                                            </s:if>
                                        	</s:if>
                                        	<s:if test="isKpfBased==1">
                                            	{name:'invoiceNo',index:'invoiceNo',width:150,sortable:false},
                                            </s:if>
                                           <s:if test="type == 'pmtnt'">
                                            {name: 'receiver', index: 'receiver', sortable: false},
                                            {name: 'c.code', index: 'c.code', sortable: false},
                                            </s:if>
                                            <s:if test="type == 'pmtnr'  && currentTenantId!='pratibha'">
                                            {name: 'receiver', index: 'receiver', sortable: false},
                                            {name: 'c.code', index: 'c.code', sortable: false},
                                    	</s:if>
                                   				
                                           				 //{name: 'mtntReceiptNumber', index: 'mtntReceiptNumber', sortable: false},
                                            {name: 'truckId', index: 'truckId',sortable: false},
                                            {name: 'driverName', index: 'driverName', sortable: false},
                                            <s:if test="isKpfBased==1">
                                            <s:if test="currentTenantId!='iffco'"> 
                                       		{name: 'client', index: 'client', width: 150, sortable: false},
                                            </s:if>
                                       		</s:if>
                                       		<s:if test="type == 'pmtnt'">
                                         	 <s:if test="currentTenantId!='pratibha'">
	                                       	 {name: 'totalMTNTBags', index: 'totalMTNTBags',sortable: false, align: 'right'},  </s:if>
	                                         <s:if test="currentTenantId!='pratibha'">
	                                       	 {name: 'totalMTNTGrossWeight', index: 'totalMTNTGrossWeight', sortable: false, align: 'right'},
	                                       	 </s:if>
	                                     	 </s:if>
                                       		<s:if test="type == 'pmtnr' ">
                                       	     <s:if test="currentTenantId!='pratibha'">
                                            {name: 'totalMTNTGrossWeight', index: 'totalMTNTGrossWeight', sortable: false, align: 'right'}, </s:if>
                                            {name: 'totalMTNRGrossWeight', index: 'totalMTNRGrossWeight', sortable: false, align: 'right'},
                                            <s:if test="currentTenantId!='pratibha'">
                                            {name: 'transportationLoss', index: 'transportationLoss', sortable: false, align: 'right'}, </s:if>
                                            </s:if>
                                            <s:if test="isKpfBased==1">
                                            <s:if test="currentTenantId!='iffco'"> 
		                                            {name: 'totalAmt', index: 'totalAmt', width: 150, sortable: false,align: 'right'},
		                                            {name: 'totalLabourCost', index: 'totalLabourCost', width: 150, sortable: false,align: 'right'},
		                                            {name: 'transportCost', index: 'transportCost', width: 150, sortable: false,align: 'right'},
		                                            {name: 'finalAmt', index: 'finalAmt', width: 150, sortable: false,align: 'right'},
                                            </s:if>
		                                            </s:if>           /*  <s:if test="type == 'pmtnt'">
			                                           {name: 'transType', index: 'transType', sortable: false, align: 'center'}
			                                           </s:if> */
                                        ],
                                        height: 301,
                                        width: $("#baseDiv").width(),
                                        scrollOffset: 19,
                                        rowNum: 10,
                                        shrinkToFit:true, 
                                        rowList: [10, 25, 50],
                                        viewrecords: true,
                                        sortname: 'id',
                                        sortorder: "desc",
                                        subGrid: true,
                                        subGridOptions: {
                                            "plusicon": "glyphicon-plus-sign",
                                            "minusicon": "glyphicon-minus-sign",
                                            "openicon": "glyphicon-hand-right",
                                        },
                                        subGridRowExpanded: function (subgrid_id, row_id) {
                                            var subgrid_table_id, pager_id;
                                            subgrid_table_id = subgrid_id + "_t";
                                            pager_id = "p_" + subgrid_table_id;
                                            $("#" + subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table> <div id='"+pager_id+"' class='scroll'></div>");
                                        var ret = jQuery("#detail").jqGrid('getRowData', row_id);
                                            var url = "<%=request.getParameter("url2")%>?type=<%=request.getParameter("type")%>&id=" + row_id;

                                            jQuery("#" + subgrid_table_id).jqGrid(
                                                    {
                                                        url: "<%=request.getParameter("url2")%>?type=<%=request.getParameter("type")%>&id=" + row_id,
                                                        pager: pager_id,
                                                        datatype: "json",
                                                        colNames: [
                                                                   <s:if test="isKpfBased=='1'">      
                                                                  		'<s:text name="sno"/>',
                                                                	</s:if>
                                                        			<s:if test="currentTenantId=='lalteer'">   
                                                        				'<s:text name="farmerName"/>',
                                                        			</s:if>
			                                                        '<s:text name="product"/>',
			                                                        '<s:text name="Unit"/>',
			                                                        '<s:property value="%{getLocaleProperty('varietyName')}" />' ,
			                                                        <s:if test="currentTenantId!='lalteer'">
			                                                        	'<s:text name="gradeName"/>',
			                                                        </s:if>
			                                                        	<s:if test="isKpfBased==1"> 
                                                       					 '<s:property value="%{getLocaleProperty('uom')}" />' ,
                                                       				</s:if>
                                                       				
                                                       											// '<s:text name="unit"/>',
			                                                         <s:if test="isKpfBased!=1"> 
			                                                     	//'<s:property value="%{getLocaleProperty('villageNames')}" />' ,
			                                                 /*     		<s:if test="type == 'pmtnt'">
                                        									'<s:property value="%{getLocaleProperty('villageNames')}"/>',
                                        								</s:if> */
                                        					/* 			<s:if test="type == 'pmtnr'">
                                        									'<s:property value="%{getLocaleProperty('senderWarehouse')}"/>',
                                        								</s:if> */
			                                                       </s:if>     
                                  	                               <s:if test="type == 'pmtnt'">
                                  	                               <s:if test="currentTenantId!='pratibha'">  
                                        						   '<s:property value="%{getLocaleProperty('noofBags')}" />' ,    </s:if>
                             	                                   '<s:property value="%{getLocaleProperty('grossWt')}" />' ,	
                             	                                   </s:if>
                             	                                   <s:if test="type == 'pmtnr'">
			                                                       <s:if test="currentTenantId!='pratibha'">  
			                                                       '<s:property value="%{getLocaleProperty('noofBagsTransferred')}"/>',
			                                                       '<s:property value="%{getLocaleProperty('grossWeightsTransferred')}"/>',			                                                     
			                                                       '<s:property value="%{getLocaleProperty('noofBagsRecieved')}"/>',
			                                                       </s:if>			                                            
			                                                       '<s:property value="%{getLocaleProperty('grossWeightsRecieved')}" />' ,
			                                                        </s:if>
			                                                        <s:if test="isKpfBased=='1'">
				                                                        '<s:property value="%{getLocaleProperty('pricePerUnit')}" />' ,
				                                                        '<s:property value="%{getLocaleProperty('subTotal')}" />' ,
			                                                        </s:if>
                                                        ],
                                                        colModel: [
                                                            		<s:if test="isKpfBased=='1'"> 
       																	{name:'sno',index:'sno',width:50,sortable:false},
       																</s:if> 
		                                                            <s:if test="currentTenantId=='lalteer'">  
		                                                           		{name: 'farmerName', index: 'farmerName', sortable: false},
		                                                            </s:if> 
		                                                            {name: 'productName', index: 'productName', sortable: false},
		                                                            {name: 'unit', index: 'unit', sortable: false},
		                                                            {name: 'varietyName', index: 'varietyName', sortable: false},
		                                                            <s:if test="currentTenantId!='lalteer'">  
		                                                            	{name: 'gradeName', index: 'gradeName', sortable: false},
		                                                            </s:if> 
		                                                            	<s:if test="isKpfBased==1">
		                                                            	{name: 'uom', index: 'uom', sortable: false, align: 'left', width: 150},
		                                                            </s:if> 
		                                                           							// {name: 'unit', index: 'unit', sortable: false},
		                                                           							//{name: 'villageName', index: 'villageName', sortable: false},
		                                                             <s:if test="isKpfBased!=1"> 
		                                                        /*      	<s:if test="type == 'pmtnt'">
		                                                           			{name: 'villageName', index: 'villageName', sortable: false},
		                                                           		</s:if> */
		                                                 /*           		<s:if test="type == 'pmtnr'">
		                                                           		{name: 'senderWarehouse', index: 'senderWarehouse', sortable: false},
		                                                           		</s:if> */
		                                                           </s:if>  
		                                                             <s:if test="type == 'pmtnt'">
		                                                             <s:if test="currentTenantId!='pratibha'">  
		                                                             {name: 'totalMTNTBags', index: 'totalMTNTBags',sortable: false, align: 'right'}, </s:if>
		                                                             {name: 'totalMTNTGrossWeight', index: 'totalMTNTGrossWeight', sortable: false, align: 'right'},
		                                                             </s:if>
		                                                            <s:if test="type == 'pmtnr'">
		                                                            <s:if test="currentTenantId!='pratibha'">  
		                                                            {name: 'mtntNoOfBags', index: 'mtntNoOfBags', sortable: false, align: 'right'},
		                                                            {name: 'mtntGrossWeight', index: 'mtntGrossWeight', sortable: false, align: 'right'},
		                                                            {name: 'mtnrNoOfBags', index: 'mtnrNoOfBags', sortable: false, align: 'right'},
		                                                            </s:if>
		                                                            {name: 'mtnrGrossWeight', index: 'mtnrGrossWeight', sortable: false, align: 'right'},
		                                                            </s:if>
		                                                            <s:if test="isKpfBased=='1'">
			                                                            {name: 'pricePerUnit', index: 'pricePerUnit', sortable: false, width: 150,align: 'right',},
			                                                            {name: 'subTotal', index: 'subTotal', sortable: false, align: 'right', width: 150},
		                                                            </s:if>
		                                                           
                                                        ],
                                                        scrollOffset: 0,
                                                        sortname: 'id',
                                                       shrinkToFit:true, 
                                                        height: '100%',
                                                        rowNum:10,
                                                        rowList: [10, 25, 50],
                                                        autowidth: true,
                                                    });
                                            jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false});
                                           // jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
                    					    jQuery("#"+subgrid_id).parent().css("width","100%");
                    					    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
                    					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
                    					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
                    					    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");

                                        },
                                        onSortCol: function (index, idxcol, sortorder) {
                                            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                                                    && this.p.colModel[this.p.lastsort].sortable !== false) {
                                                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
                                            }
                                        }
                                    });
                            $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
                            jQuery("#detail").jqGrid('navGrid', '#pagerForDetail', {edit: false, add: false, del: false, search: false, refresh: false});
                            jQuery("#detail").jqGrid('hideCol', columnNames.split(",")).trigger('reloadGrid');

                            colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
                            $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
                                    ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
                                var cmi = colModel[i], colName = cmi.name;

                                if (cmi.sortable !== false) {
                                    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
                                } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
                                    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
                                }
                            });
                            jQuery("#detail").setGridWidth($("#baseDiv").width());
                        }

                        jQuery("#generate").click(function () {
                            reloadGrid();
                        });

                        jQuery("#clear").click(function () {
                        	clear();
                        });

                        function reloadGrid() {
                        	var d1=	jQuery('#daterange').val();
                    		var d2= d1.split("-");
                    		//	alert(d1);
                    		var value1= d2[0];
                    		//alert(value1);
                    		var value2= d2[1];
                    		//alert(value2);
                    	document.getElementById("hiddenStartDate").value=value1;
                    	
                    	document.getElementById("hiddenEndDate").value=value2;
                    	
                    		var startDate=new Date(document.getElementById("hiddenStartDate").value);
                    	//	alert(startDate);
                    		var endDate=new Date(document.getElementById("hiddenEndDate").value);
                
                        if (startDate > endDate    ) {
                                alert('<s:text name="date.range"/>');
                            } else {

                                if (type == 'pmtnt') {
                    				<s:if test="currentTenantId!='livelihood'">
                                   // mtntReceiptNumber = document.getElementById("mtntReceiptNumber").value;
                                    seasonId = document.getElementById("seasonId").value;</s:if>
                                }

                                if (type == 'pmtnr') {<s:if test="currentTenantId!='livelihood'">
                                  //  mtnrReceiptNumber = document.getElementById("mtnrReceiptNumber").value;
                                    seasonId = document.getElementById("seasonId").value;</s:if>
                                }
                                if(type=='procTrans'||type=='procRecp'){
                                	proCenter=$('#proCenter').val();
                                    ginning=$('#ginning').val();
                                	<s:if test="currentTenantId!='livelihood'">
                                    season=$('#season').val();  </s:if>                                 
                        			}else{
                        				fieldStaff = $('#fieldStaff').val();
                                        cooperativeId =$('#cooperativeId').val();
                        			}
                            	
                                truckId = $('#truckId').val();
                                driverName = $('#driverName').val();
                               
                                /* if (type == 'pmtnt') {
                                transType = document.getElementById("transType").value;
                                } */
                                jQuery("#detail").jqGrid('setGridParam', {url: detailAction, page: 1})
                                        .trigger('reloadGrid');
                                
                            }
                        }

                        function onFilterData(type){
                			
                			 if(type=='procTrans' || type=='procRecp'){
                			callAjaxMethod("productTransferReport_populateProCenterList.action","proCenter");
                			callAjaxMethod("productTransferReport_populateGinningList.action","ginning");
                			callAjaxMethod("productTransferReport_populateSeasonList.action","season");
                			 }
                			 else{
                				 callAjaxMethod("productTransferReport_populateAgentList.action","fieldStaff");	 
                				 callAjaxMethod("productTransferReport_populateSeasonList.action","seasonId");
                     			callAjaxMethod("productTransferReport_populateCooperativeList.action","cooperativeId");
                			 }
                			
                		}
                	
                		function callAjaxMethod(url,name){
                			var cat = $.ajax({
                				url: url,
                				async: true, 
                				type: 'post',
                				success: function(result) {
                					insertOptions(name,JSON.parse(result));
                				}        	

                			});
                			
                		}
                		
                		
                		
                		function clear() {
                            /* if (type == 'pmtnt')
                                document.getElementById("mtntReceiptNumber").value = ""; */
                          /*   if (type == 'pmtnr')
                                document.getElementById("mtnrReceiptNumber").value = ""; */
                                if(type=='procTrans' || type=='procRecp'){
                                	jQuery("#proCenter").selectedIndex=0;
                                    jQuery("#ginning").selectedIndex=0;
                    				<s:if test="currentTenantId!='livelihood'">
                                    jQuery("#season").selectedIndex=0;</s:if>
                                }
                                else{
                                jQuery("#cooperativeId").selectedIndex=0;
                                }
                            //document.getElementById("cooperativeId").selectedIndex = 0;
                            jQuery("#truckId").val("");
                            jQuery("#driverName").val("");
                            if (type == 'pmtnt')
                            //document.getElementById("transType").selectedIndex = 0;
                            	<s:if test="currentTenantId!='livelihood'">
                            jQuery("#seasonId").selectedIndex=0;</s:if>
                            jQuery("#fieldStaff").selectedIndex=0;
                            
                            //document.getElementById("seasonId").selectedIndex = 0;
                            document.form.action = listAction;
                            document.form.submit();

                        }

                    function getAjaxRecord()     {
                    	$.post("<%=request.getParameter("url3")%>?type=<%=request.getParameter("type")%>", function (result) {
                                            columnNames = result;
                                            if(type=='procTrans' || type=='procRecp'){
                                            	loadProcurementCenterGrid();
                                            }
                                            else{
                                            loadGrid();
                                            }
                                        });
                                    }

                    function loadProcurementCenterGrid(){
                        jQuery("#detail").jqGrid({
                                                url: detailAction,
                                                pager: '#pagerForDetail',
                                                datatype: "json",
                                                mtype: 'POST',
                                                postData: {
                                                	"startDate" : function(){
                           		                     return document.getElementById("hiddenStartDate").value;           
                           		                    },		
                           		                    "endDate" : function(){
                           				               return document.getElementById("hiddenEndDate").value;
                           			                 },
                                                    "proCenter":function(){
                                                    	return proCenter;
                                                    },
                                                    "ginning":function(){
                                                    	return ginning;
                                                    },
                                                    "truck": function () {
                                                    	 return truckId!=undefined ? truckId : "";
                                                    },<s:if test="currentTenantId!='livelihood'">
                                                    "season": function() {
                                                    	return season;
                                                    },</s:if>
                                                    /* "selectedFieldStaff": function () {
                                                    	  return fieldStaff!= undefined ? fieldStaff : "";

                                                    }, */
                                                    <s:if test='branchId==null'>
                              		             		 "branchIdParma":function(){
                              		             			 var branch=$('#branchIdParma').val();
                              		             			 return branch!= undefined ? branch : "";
                              			        		 },
                              			         </s:if>
                              			         <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
                              					  		"subBranchIdParma" : function(){
                              						 		return document.getElementById("subBranchIdParam").value;
                              					 		},
                              					  </s:if>
                                                },
                                            colNames: [
													<s:if test="currentTenantId=='chetna'"> 
                                                       <s:if test='branchId==null'>
        													'<s:text name="%{getLocaleProperty('app.branch')}"/>',
        												</s:if>
        												<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
        													'<s:text name="app.subBranch"/>',
        												</s:if>  
        													
        													'<s:property value="%{getLocaleProperty('date')}"/>',
        													<s:if test="currentTenantId=='chetna' ">
        													'<s:property value="%{getLocaleProperty('season')}"/>',
        													'<s:property value="%{getLocaleProperty('trRecptNum')}"/>',
        													</s:if>
        													<s:if test="type == 'procRecp'">
        													'<s:property value="%{getLocaleProperty('RerecptNum')}"/>',
        													</s:if>  
        													
        													 '<s:property value="%{getLocaleProperty('agentName')}"/>',
                                                		'<s:property value="%{getLocaleProperty('cooperative')}"/>',
                                                		'<s:property value="%{getLocaleProperty('ginning')}"/>',
                                                		
    													'<s:property value="%{getLocaleProperty('driver')}"/>',
    													'<s:property value="%{getLocaleProperty('transporter')}"/>',
    													/* <s:if test="type == 'procRecp'">
                                                		'<s:property value="%{getLocaleProperty('icsName')}" />' ,
                                                		</s:if> */
                                                		'<s:property value="%{getLocaleProperty('pp.name')}" />' ,
                                                		/* '<s:property value="%{getLocaleProperty('variety')}"/>',
                                                		'<s:property value="%{getLocaleProperty('grade')}"/>', */
                                                		'<s:property value="%{getLocaleProperty('truckId')}"/>',
                                                		/* <s:if test="type == 'procRecp'">
                                                		'<s:property value="%{getLocaleProperty('heapName')}"/>',
                                                		</s:if> */
                                                		'<s:property value="%{getLocaleProperty('transBags')}"/>',
                                                		'<s:property value="%{getLocaleProperty('transWeight')}" /> (<s:property value="%{getLocaleProperty('quintal')}"/>)',
                                                		<s:if test="type == 'procRecp'">
                                                			'<s:property value="%{getLocaleProperty('receiveBags')}"/>',
                                                		</s:if>
        	                                        	
        	                                        	<s:if test="type == 'procRecp'">
        	                                        		'<s:property value="%{getLocaleProperty('receiveWeight')}"/> (<s:property value="%{getLocaleProperty('quintal')}"/>)',
        	                                        		'<s:property value="%{getLocaleProperty('diffQty')}"/>',                                            			
                                            				'<s:property value="%{getLocaleProperty('photo')}"/>',
                                            			</s:if>
                                                			'<s:property value="%{getLocaleProperty('detail')}"/>',
                                                	</s:if><s:else>
                                                			
                                                			<s:if test='branchId==null'>
        													'<s:text name="%{getLocaleProperty('app.branch')}"/>',
        												</s:if>
        												<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
        													'<s:text name="app.subBranch"/>',
        												</s:if> 
        													<s:if test="type == 'procTrans'">
        													'<s:property value="%{getLocaleProperty('date')}"/>',
        													'<s:property value="%{getLocaleProperty('trRecptNum')}"/>',
        													'<s:property value="%{getLocaleProperty('truckId')}"/>',
        													'<s:property value="%{getLocaleProperty('driver')}"/>',
        													'<s:property value="%{getLocaleProperty('cooperative')}"/>',
                                                    		'<s:property value="%{getLocaleProperty('ginning')}"/>',
                                                    		'<s:property value="%{getLocaleProperty('transporter')}"/>',
                                                    		'<s:property value="%{getLocaleProperty('pp.name')}" />' ,
                                                    		'<s:property value="%{getLocaleProperty('variety')}"/>',
                                                    		'<s:property value="%{getLocaleProperty('grade')}"/>',
                                                    		'<s:property value="%{getLocaleProperty('transWeight')}" />',
                                                    		'<s:property value="%{getLocaleProperty('detail')}"/>',
                                                    		</s:if>
                                                    		<s:if test="type == 'procRecp'">
                                                    		'<s:property value="%{getLocaleProperty('ginning')}"/>',
                                                    		'<s:property value="%{getLocaleProperty('RerecptNum')}"/>',
                                                    		'<s:property value="%{getLocaleProperty('date')}"/>',
                                                    		'<s:property value="%{getLocaleProperty('truckId')}"/>',
                                                    		'<s:property value="%{getLocaleProperty('cooperative')}"/>',
                                                    		/* '<s:property value="%{getLocaleProperty('pp.name')}" />' , */
                                                    		'<s:property value="%{getLocaleProperty('transferWeight')}" />',
           	                                        		'<s:property value="%{getLocaleProperty('receiveWeight')}"/>',
           	                                        		'<s:property value="%{getLocaleProperty('receiveBags')}"/>',
           	                                        		'<s:property value="%{getLocaleProperty('detail')}"/>',
                                                    		</s:if>
                                                	</s:else>
        	                                       
                                                ],
                                                colModel: [
													<s:if test="currentTenantId=='chetna'"> 
                                                           <s:if test='branchId==null'>
                               					   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
                               					   			value: '<s:property value="parentBranchFilterText"/>',
                               					   			dataEvents: [{
                               					   			            type: "change",
                               					   			            fn: function () {
                               					   			             	getSubBranchValues($(this).val())}
                               					   			        }] }},	   				   		
                               					   </s:if>
                               					   <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
                               					   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
                              					   </s:if>  
        	                                            {name: 'mtntDate', index: 'mtntDate',sortable: false},
        	                                        	<s:if test="currentTenantId=='chetna' ">  
        	                                            {name:'season', index:'season',sortable:false},
        	                                            {name:'trRecptNum', index:'trRecptNum',sortable:false},
        	                                            </s:if>
        	                                            <s:if test="type == 'procRecp'">
        	                                            {name:'reRecptNum', index:'reRecptNum',sortable:false},
        	                                            </s:if>
        	                                           
        	                                            {name:'agentName', index:'agentName',sortable:false},
        	                                            {name: 'proCenter', index: 'proCenter', sortable: false},
        	                                            {name: 'ginning', index: 'ginning', sortable: false},
        	                                       
        	                                            {name: 'driver', index: 'driver',sortable: false},
        	                                            {name: 'transport', index: 'transport',sortable: false},
        	                                            /* <s:if test="type == 'procRecp'">
       	                                            	{name: 'ics', index: 'ics',sortable: false},
       	                                            	</s:if> */
       	                                            	{name: 'product', index: 'product', sortable: false},
       	                                            	/* {name: 'variety', index: 'variety', sortable: false},
       	                                            	{name: 'grade', index: 'grade', sortable: false}, */
	                                                    {name: 'truckId', index: 'truckId',sortable: false},
	                                                   /*  <s:if test="type == 'procRecp'">
	                                                    {name:'heapName', index: 'heapName', sortable: false},
	                                                    </s:if> */
	                                                    {name: 'transBags', index: 'transBags',sortable: false},
	                                                    {name: 'transWeight', index: 'transWeight', sortable: false, align: 'right'},
	                                                	<s:if test="type == 'procRecp'">
	                                                    {name: 'receiveBags', index: 'receiveBags',sortable: false, align: 'right'},
	                                                    </s:if>
	                                                   
	                                                    <s:if test="type == 'procRecp'">
	                                                    {name: 'receiveWeight', index: 'receiveWeight',sortable: false, align: 'right'},
	                                                    {name: 'diffQty', index: 'diffQty',sortable: false, align: 'right'},
	                                                    {name:'photo', index:'photo',sortable:false,align:'center'},
	                                                    </s:if>
	                                                   
	                                                    {name: 'detail', index: 'detail',sortable: false, align: 'right'},
	                                                  </s:if>
	                                                    <s:else>
	                                                    <s:if test='branchId==null'>
                               					   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
                               					   			value: '<s:property value="parentBranchFilterText"/>',
                               					   			dataEvents: [{
                               					   			            type: "change",
                               					   			            fn: function () {
                               					   			             	getSubBranchValues($(this).val())}
                               					   			        }] }},	   				   		
                               					   </s:if>
                               					   <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
                               					   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
                              					   </s:if>  
	                                                    <s:if test="type == 'procTrans'">
	                                                    {name: 'mtntDate', index: 'mtntDate',sortable: false},
	                                                    {name:'trRecptNum', index:'trRecptNum',sortable:false},
	                                                    {name: 'truckId', index: 'truckId',sortable: false},
	                                                    {name: 'driver', index: 'driver',sortable: false},
	                                                    {name: 'proCenter', index: 'proCenter', sortable: false},
	                                                    {name: 'ginning', index: 'ginning', sortable: false},
	                                                    {name: 'transport', index: 'transport',sortable: false},
	                                                    {name: 'product', index: 'product', sortable: false},
	                                                    {name: 'variety', index: 'variety', sortable: false},
       	                                            	{name: 'grade', index: 'grade', sortable: false},
       	                                             {name: 'transWeight', index: 'transWeight', sortable: false, align: 'right'},
       	                                          {name: 'detail', index: 'detail',sortable: false, align: 'right'},
                                                		</s:if>
                                                		<s:if test="type == 'procRecp'">
                                                		 {name: 'ginning', index: 'ginning', sortable: false},
                                                		 {name:'reRecptNum', index:'reRecptNum',sortable:false},
                                                		{name: 'mtntDate', index: 'mtntDate',sortable: false},
                                                		 {name: 'truckId', index: 'truckId',sortable: false},
                                                		 {name: 'proCenter', index: 'proCenter', sortable: false},
                                                		/*  {name: 'product', index: 'product', sortable: false}, */
        	                                            	 {name: 'transWeight', index: 'transWeight', sortable: false, align: 'right'},
        	                                            	 {name: 'receiveWeight', index: 'receiveWeight',sortable: false, align: 'right'},
        	                                            	 {name: 'receiveBags', index: 'receiveBags',sortable: false, align: 'right'},
        	                                            	 {name: 'detail', index: 'detail',sortable: false, align: 'right'},
                                                		</s:if>
	                                                    
	                                                    
	                                                    </s:else>
                                                ],
                                                height: 301,
                                                width: $("#baseDiv").width(),
                                                scrollOffset: 19,
                                                rowNum: 10,
                                                shrinkToFit:true, 
                                                rowList: [10, 25, 50],
                                                viewrecords: true,
                                                sortname: 'id',
                                                sortorder: "desc",
                                                subGrid: false,
                                                	
                                                onSortCol: function (index, idxcol, sortorder) {
                                                    if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                                                            && this.p.colModel[this.p.lastsort].sortable !== false) {
                                                        $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
                                                    }
                                                },
                                                /* onSelectRow: function(id){ 
                                                	if(type == 'procRecp'){
                                                		detailPopup(id);
                                                	}
                                                	
                                                } */
                                            });
                                    $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
                                    jQuery("#detail").jqGrid('navGrid', '#pagerForDetail', {edit: false, add: false, del: false, search: false, refresh: false});
                                    jQuery("#detail").jqGrid('hideCol', columnNames.split(",")).trigger('reloadGrid');

                                    colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
                                    $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
                                            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
                                        var cmi = colModel[i], colName = cmi.name;

                                        if (cmi.sortable !== false) {
                                            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
                                        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
                                            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
                                        }
                                    });
                                    jQuery("#detail").setGridWidth($("#baseDiv").width());
                    }
                                jQuery("#minus").click(function ()     {
                                        var type = '<%=request.getParameter("type")%>';

                                        if (document.getElementById(document.getElementById("fieldl").selectedIndex) != null && (document.getElementById(document.getElementById("fieldl").selectedIndex).className == "" || document.getElementById(document.getElementById("fieldl").selectedIndex).className == " ")) {
                                            removeFields();
                                            var divOption = document.getElementById("fieldl");
                                            if (divOption != null) {
                                                if (type == "pmtnt") {
                                                    if (divOption.selectedIndex == 1) {
                                                        document.getElementById("startDate").value = document.getElementById("hiddenStartDate").value;
                                                        document.getElementById("endDate").value = document.getElementById("hiddenEndDate").value;
                                                    }
                                                } else {
                                                    if (divOption.selectedIndex == 4) {
                                                        document.getElementById("startDate").value = document.getElementById("hiddenStartDate").value;
                                                        document.getElementById("endDate").value = document.getElementById("hiddenEndDate").value;
                                                    }
                                                }
                                            }
                                            divOption.selectedIndex = 0;
                                            reloadGrid();
                                        }
                                    });

                                    jQuery("#plus").click(function () {
                                        showFields();
                                    });

                                });

                                function exportXLS() {
                                    if (isDataAvailable("#detail")) {
                                        jQuery("#detail").setGridParam({postData: {rows: 0}});
                                        jQuery("#detail").jqGrid('excelExport', {url: populateAction});
                                }     else {
                                        alert('<s:text name="export.nodata"/>');
                                    }
                                }
                                function buttonEdcCancel(){
                    				//refreshPopup();
                    				document.getElementById("model-close-edu-btn").click();	
                    		     }
                                
                                function buttonDataCancel(){
                                	document.getElementById("model-close-data-btn").click();
                                	document.getElementById("detail-close-data-btn").click();
                                }
                                function popupWindow(ids) {
                    				try{
                    					var str_array = ids.split(',');
                    					$("#mbody").empty();
                    					var mbody="";
                    					for(var i = 0; i < str_array.length; i++){
                    						//<img class="slidesjs-slide" src="sensitizingReport_populateImage.action?id='+str_array[i]+'" slidesjs-index="0"/>');
                    						if(i==0){
                    							mbody="";
                    							mbody="<div class='item active'>";
                    							mbody+='<img src="productTransferReport_populateImage.action?imgId='+str_array[i]+'"/>';
                    							mbody+="</div>";
                    						}else{
                    							mbody="";
                    							mbody="<div class='item'>";
                    							mbody+='<img src="productTransferReport_populateImage.action?imgId='+str_array[i]+'"/>';
                    							mbody+="</div>";
                    						}
                    						$("#mbody").append(mbody);
                    						
                    					 }
                    					
                    					//$("#mbody").first().addClass( "active" );
                    					
                    					document.getElementById("enableModal").click();	
                    				}
                    				catch(e){
                    					alert(e);
                    					}
                    				
                    			}
                                function FarmerDetailsPopup(receiptNo){
                                	try{
                                		var jsonData="";
                                		$("#detailDataBody").empty();
                                		var bodyData="";
                                		$.ajax({
                                			type: "POST",
                                			async: false,
                                			url: "productReceiptionReport_populateFarmerDetailData.action",
                                			data:{mtntReceiptNumber:receiptNo},
                                			success: function(result){
                                				var jsonValue=$.parseJSON(result);
												$.each(jsonValue,function(index, value){
													var tr=$("<tr/>");
													
													var td0=$("<td/>");
													td0.text(value.product);
													
													var td1=$("<td/>");
													td1.text(value.grade);

													
													var td2=$("<td/>");
													td2.text(value.variety);

													
													var td3=$("<td/>");
													td3.text(value.goodQty);

													
													var td4=$("<td/>");
													td4.text(value.Name);

													var td5=$("<td/>");
													td5.text(value.City_Name);
													
													var td6=$("<td/>");
													td6.text(value.Village_Name);
													
													var td7=$("<td/>");
													td7.text(value.Group_Name);
													
													var td8=$("<td/>");
													td8.text(value.farmerCode);
													
													tr.append(td8);
													tr.append(td4);
                    					       		tr.append(td5);
                    					       		tr.append(td6);
                    					       		tr.append(td7);
                    					       		tr.append(td0);
                    					       		tr.append(td1);
                    					       		tr.append(td2);
                    					       		tr.append(td3);
                    					       		$("#detailDataBody").append(tr);
												});
                                			}
                                		});
                                		document.getElementById("enableDetailPopup").click();	
                                	}
                                	catch(e){
                    					alert(e);
                    					}
                                	
                                }
                                
                                function detailPopup(id){
                                	try{
                                		var jsonData="";
                                		$("#detailDataBody").empty();
                                		var bodyData="";
                                		$.ajax({
                                			type: "POST",
                                			async: false,
                                			url: "productReceiptionReport_populateDetailData.action",
                                			data:{pmtId:id},
                                			success: function(result){
                                				var jsonValue=$.parseJSON(result);
												$.each(jsonValue,function(index, value){
													if(tenant=='chetna'){
													var tr=$("<tr/>");
													var td=$("<td/>");
													td.text(value.Warehouse);
													var td1=$("<td/>");
													td1.text(value.Ginning);
													var td2=$("<td/>");
													td2.text(value.Product);
													var td3=$("<td/>");
													td3.text(value.ICS);
													var td4=$("<td/>");
													td4.text(value.TransBag);
													var td5=$("<td/>");
													td5.text(value.TransQty);
													var td6=$("<td/>");
													td6.text(value.RecBag);
													var td7=$("<td/>");
													td7.text(value.RecQty);
													var td8=$("<td/>");
													td8.text(value.Heap);
													
													tr.append(td);
                    					       		tr.append(td1);
                    					       		tr.append(td2);
                    					       		tr.append(td3);
                    					       		tr.append(td4);
                    					       		tr.append(td5);
                    					       		tr.append(td6);
                    					       		tr.append(td7);
                    					       		tr.append(td8);
                    					       		$("#detailDataBody").append(tr);
													}else{
														var tr=$("<tr/>");
														var td=$("<td/>");
														td.text(value.Warehouse);
														var td1=$("<td/>");
														td1.text(value.Ginning);
														var td2=$("<td/>");
														td2.text(value.Product);
														var td4=$("<td/>");
														td4.text(value.TransBag);
														var td5=$("<td/>");
														td5.text(value.TransQty);
														var td6=$("<td/>");
														td6.text(value.RecBag);
														var td7=$("<td/>");
														td7.text(value.RecQty);
														
														tr.append(td);
	                    					       		tr.append(td1);
	                    					       		tr.append(td2);
	                    					       		tr.append(td4);
	                    					       		tr.append(td5);
	                    					       		tr.append(td6);
	                    					       		tr.append(td7);
	                    					       		$("#detailDataBody").append(tr);
													}
												});
                                			}
                                		});
                                		document.getElementById("enableDetailPopup").click();	
                                	}
                                	catch(e){
                    					alert(e);
                    					}
                                	
                                }
                                function popupFarmerData(val,product,ics) {
                                	try{
                                		var jsonFarmerData = "";
                    					var str_array = val.split(',');
                    					$("#mDatabody").empty();
                    					//alert(str_array);
                    					var mDatabody="";
                    					for(var i = 0; i < str_array.length; i++){
                    						$.ajax({
                    							 type: "POST",
                    					        async: false,
                    					        url: "productTransferReport_populateFarmerData.action",
                    					        data: {id : val,product : product,icsName:ics},
                    					        success: function(result) {
                    					        	var jsonData = $.parseJSON(result);
                    					        	console.log(jsonData);
                    					        	$.each(jsonData, function(index, value) {
                    					        		if(tenant=='chetna'){
                    					        		var tr = $("<tr/>");		
                    					        		var td = $("<td/>");
                        					       	 	td.text(value.First_Name);
                        					       		var td1 = $("<td/>");
                        					       	 	td1.text(value.Last_Name);
                        					       		var td2 = $("<td/>");
                        					       		td2.text(value.Village_Name);
                        					       		var td3 = $("<td/>");
                        					       		td3.text(value.City_Name);
                        					       		var td4 = $("<td/>");
                        					       		td4.text(value.Group_Name);
                        					       		var td5 = $("<td/>");
                        					       		td5.text(value.Ics_Name);
                        					     		var td10 = $("<td/>");
                        					       		td10.text(value.fpo);
                        					       		var td6 = $("<td/>");
                        					       		td6.text(value.product);
                        					       		var td11 = $("<td/>");
                        					       		td11.text(value.variety);
                        					       		var td7 = $("<td/>");
                        					       		td7.text(value.grade);
                        					       		var td8 = $("<td/>");
                        					       		td8.text(value.noOfBags);
                        					       		var td9 = $("<td/>");
                        					       		td9.text(value.grossBags);
                        					       		
                        					       		

                        					       		tr.append(td);
                        					       		tr.append(td1);
                        					       		tr.append(td2);
                        					       		tr.append(td3);
                        					       		tr.append(td4);
                        					       		tr.append(td5);
                        					       		tr.append(td10);
                        					       		tr.append(td6);
                        					       		tr.append(td11);
                        					       		tr.append(td7);
                        					       		tr.append(td8);
                        					       		tr.append(td9);
                        					       		
                        					       	 $("#mDatabody").append(tr);
                    					        	}else{
                    					        		var tr = $("<tr/>");		
                    					        		var td = $("<td/>");
                        					       	 	td.text(value.First_Name+" "+value.Last_Name);
                        					       	 var td10 = $("<td/>");
                     					       		td10.text(value.Farmer_Code);
                        					       		var td2 = $("<td/>");
                        					       		td2.text(value.Village_Name);
                        					       		var td3 = $("<td/>");
                        					       		td3.text(value.City_Name);
                        					       		var td4 = $("<td/>");
                        					       		td4.text(value.Group_Name);
                        					       		var td6 = $("<td/>");
                        					       		td6.text(value.product);
                        					       		var td11 = $("<td/>");
                        					       		td11.text(value.variety);
                        					       		var td7 = $("<td/>");
                        					       		td7.text(value.grade);
                        					       		var td8 = $("<td/>");
                        					       		td8.text(value.noOfBags);
                        					       		var td9 = $("<td/>");
                        					       		td9.text(value.grossBags);
                        					       		
                        					       		

                        					       		tr.append(td);
                        					       		tr.append(td10);
                        					       		tr.append(td2);
                        					       		tr.append(td3);
                        					       		tr.append(td4);
                        					       		tr.append(td6);
                        					       		tr.append(td11);
                        					       		tr.append(td7);
                        					       		tr.append(td8);
                        					       		tr.append(td9);
                        					       		
                        					       	 $("#mDatabody").append(tr);
                    					        	}
                    					        	});
                    					        	
                    					        	
                    					        }
                    					       
                    						});
                    					
                    						
                    					 }
                    					
                    				
                    					document.getElementById("enableDataModal").click();	
                    				}
                    				catch(e){
                    					alert(e);
                    					}
                            	
                            	}
    </script>

	<div id="divs" align="right" class="hide">
		<s:text name="field" />
		<s:select name="fieldl" id="fieldl" list="fields" headerKey=""
			headerValue="%{getText('txt.select')}" theme="simple" />
		&nbsp;
		<button type="button" id="plus" class="fa fa-plus" aria-hidden="true"></button>
		&nbsp;
		<button type="button" id="minus" class="fa fa-minus"
			aria-hidden="true"></button>
	</div>
	<s:form name="form">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
					<div class='filterEle'>
						<label for="txt"><s:text name="startingDate" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control" />
						</div>
					</div>

					<%--  <s:if test="type == 'pmtnt'">
					<div class="filterEle">
						<label for="txt"><s:text name="receiptNo" /></label>
						<div class="form-element">
						<s:select name="mtntReceiptNumber" id="mtntReceiptNumber" list="receiptNumberList" headerKey="" 
						 headerValue="%{getText('txt.select')}" class="form-control  select2"/> 
							  <s:textfield name="mtntReceiptNumber" theme="simple" id="mtntReceiptNumber" class="form-control "/>
						</div>
					</div>
				</s:if>
			  <s:if test="type == 'pmtnr'">
			  	<div class="filterEle">
						<label for="txt"><s:text name="receiptNo" /></label>
						<div class="form-element">
						<s:select name="mtnrReceiptNumber" id="mtnrReceiptNumber" list="receiptNumberList" headerKey="" 
						 headerValue="%{getText('txt.select')}" class="form-control  select2"/> 
							  <s:textfield name="mtnrReceiptNumber" theme="simple" id="mtnrReceiptNumber" class="form-control " />
						</div>
					</div>
			 </s:if> --%>
			  <s:if test="type == 'pmtnt'">
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('userAgent')}"/></label>
					<div class="form-element">
						<s:select name="selectedFieldStaff" id="fieldStaff"
							list="{}" headerKey="" headerValue=""
							class="form-control input-sm select2" />
					</div>
				</div>
				</s:if>
				<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element ">
									<s:select name="branchIdParma" id="branchIdParma"
										list="parentBranches" headerKey="" headerValue="Select"
										cssClass="select2" onchange="populateChildBranch(this.value)" />

								</div>
							</div>
						</s:if>

						<div class="filterEle">
							<label for="branchIdParam"><s:text name="app.subBranch" /></label>
							<div class="form-element">
								<s:select name="subBranchIdParam" id="subBranchIdParam"
									list="subBranchesMap" headerKey="" headerValue="Select"
									cssClass="input-sm form-control select2" />
							</div>
						</div>

					</s:if>
					<s:else>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:select name="branchIdParma" id="branchIdParma"
										list="branchesMap" headerKey=""
										headerValue="%{getText('txt.select')}"
										cssClass="form-control input-sm select2" />
								</div>
							</div>
						</s:if>
					</s:else>
			 <s:if test="type=='procTrans' || type=='procRecp'">
			 		
			 			<s:if test="currentTenantId!='livelihood'">
						<div class="filterEle">
							<label for="txt"><s:property
									value="%{getLocaleProperty('season')}" /></label>
						
						<div class="form-element">
							<s:select name="season" id="season" list="{}"
								listKey="key" listValue="value" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control select2" />
						</div>
					</div></s:if>
					
			 		<div class="filterEle">
							<label for="txt"><s:property
									value="%{getLocaleProperty('cooperative')}" /></label>
						
						<div class="form-element">
							<s:select name="proCenter" id="proCenter" list="{}"
								listKey="key" listValue="value" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control select2" />
						</div>
					</div>
					
						<div class="filterEle">
							<label for="txt"><s:property
									value="%{getLocaleProperty('ginning')}" /></label>
						
						<div class="form-element">
							<s:select name="ginning" id="ginning" list="{}"
								listKey="key" listValue="value" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control select2" />
						</div>
					</div>
			 </s:if>
			 <s:else>
			 		<s:if test="currentTenantId!='livelihood'">
					<div class="filterEle">
						<label for="txt"><s:text name="season" /></label>
						<div class="form-element">
							<s:select name="season" id="seasonId" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control  select2" />
						</div>
					</div></s:if>
					<%-- <s:if test="type == 'pmtnt'">
						<div class="filterEle">
							<label for="txt"><s:text name="transType" /></label>
							<div class="form-element">
								<s:select name="transType" id="transType" list="transTypeList"
									listKey="key" listValue="value" headerKey=""
									headerValue="%{getText('txt.select')}"
									class="form-control select2" />
							</div>
						</div>
					</s:if>
 --%>
					
					<div class="filterEle">
						<s:if test="type == 'pmtnt'">
							<label for="txt"><s:property
									value="%{getLocaleProperty('cooperative.sender')}" /></label>
						</s:if>
						<s:else>
							<label for="txt"><s:property
									value="%{getLocaleProperty('receiver')}" /></label>
						</s:else>
						<div class="form-element">
							<s:select name="cooperative" id="cooperativeId" list="{}"
								listKey="key" listValue="value" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control select2" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:text name="driverName" /></label>
						<div class="form-element">
							<s:textfield name="driver" theme="simple" id="driverName"
								class="form-control " />
						</div>
					</div>
				</s:else>
					
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('truckId')}" /></label>
						<div class="form-element">
							<s:textfield name="truck" theme="simple" id="truckId"
								class="form-control " />
						</div>
					</div>
					
					

					<div class="filterEle" style="margin-top: 2%; margin-right: 0%;">
						<button type="button" class="btn btn-success" id="generate"
							aria-hidden="true">
							<b class="fa fa-search"></b>
						</button>
						<button type="button" class="btn btn-danger " aria-hidden="true"
							id="clear">
							<b class="fa fa-close"></b>
						</button>

					</div>
				</div>
			</section>
		</div>

	</s:form>


	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
			<!-- <div class="flexItem-2">
					<div class="summaryBlocksWrapper flex-container">
						<div class="report-summaryBlockItem">
							<span><span class="strong" id="farmerCount"></span> Farmers&nbsp;<i
								class="fa fa-user"></i></span>
						</div>
						<div class="report-summaryBlockItem">
							<span><span class="strong" id="tArea"></span> Total Area&nbsp;<i
								class="fa fa-dollar"></i></span>
						</div>
						
						<div class="report-summaryBlockItem">
							<span><span class="strong" id="totalCoc"></span> Total Cost&nbsp;<i
								class="fa fa-dollar"></i></span>
						</div>
					</div>
				</div> -->

			<div class="flexItem text-right flex-right">
				<div class="dropdown">
					<button id="dLabel" class="btn btn-primary btn-sts smallBtn"
						type="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">
						<i class="fa fa-share"></i>
						<s:text name="export" />
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right"
						aria-labelledby="myTabDrop1" id="myTabDrop1-contents">

						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div>

		</div>

		<div class="jqGridwrapper baseDiv">
			<table id="grid"></table>
			<div id="pager"></div>
		</div>

		<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>
	<s:form name="updateform" action="productTransferReport_populateFarmer">
		<s:hidden name="id" />
		  <s:hidden name="type" class="type"/>  
		<s:hidden name="currentPage" />
	</s:form>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	
	
	<button type="button" id="enableModal"
		class="hide slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>

	<div id="slideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">
					</h4>
				</div>
				<div class="modal-bodys">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						 <div class="carousel-inner" role="listbox" id="mbody">
						 	
						 </div>
						 
						 <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
						      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						      <span class="sr-only">Previous</span>
   						 </a>
					    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
					      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					      <span class="sr-only">Next</span>
					    </a>
					    
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonEdcCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>
	
	<button type="button" id="enableDataModal"
		class="hide slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideDataModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>
	
	<div id="slideDataModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-data-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">Farmer Details</h4>
				</div>

				<table class="table table-bordered table-responsive">
					<div class="modal-body">
						<thead>
							<tr>
								<!-- <td>Sno</td> -->
								<s:if test="currentTenantId=='chetna'">  
								<td>First Name</td>
								<td>Last Name</td>
								<td>Village Name</td>
								<td>Taluk</td>
								<td>SHG Name</td>
								<td>ICS Name</td>
								<td>Cooperative</td>
								<td>Product</td>
								<td>Variety</td>
								<td>Grade</td>
								<td>No Of Bags</td>
								<td>Net Weight(kg)</td>
								</s:if>
								<s:else>
								<td>Farmer Name</td>
								<td>Farmer Code</td>
								<td>Barangay</td>
								<td>Municipality</td>
								<td>Partner Organisation</td>
								<td>Product</td>
								<td>Variety</td>
								<td>Grade</td>
								<td><s:property value="%{getLocaleProperty('receiveBags')}" /></td>
								<td><s:property value="%{getLocaleProperty('transferWeight')}" /></td>
								
								</s:else>
							</tr>
						</thead>
						<tbody id="mDatabody">
						</tbody>
					</div>
				</table>

				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonDataCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>
	
	
	<button type="button" id="enableDetailPopup"
		class="hide slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideDetailModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>
	<div id="slideDetailModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="detail-close-data-btn" class="close hide"
						data-dismiss="modal">&times;</button>
						<s:if test="currentTenantId=='chetna'">  
					<h4 class="modal-title" id="mhead">Reception Data Details</h4>
					</s:if>
					<s:else>
					<h4 class="modal-title" id="mhead">Farmer Details</h4>
					</s:else>
				</div>

				<table class="table table-bordered table-responsive">
					<div class="modal-body">
						<thead>
							<tr>
							<s:if test="currentTenantId=='chetna'">  
								<td><s:property value="%{getLocaleProperty('procurementCenter')}" /></td>
								<td><s:property value="%{getLocaleProperty('ginning')}" /></td>
								<td><s:property value="%{getLocaleProperty('pp.name')}" /></td>
								<td><s:property value="%{getLocaleProperty('icsNames')}" /></td>
								<td><s:property value="%{getLocaleProperty('transBags')}" /></td>
								<td><s:property value="%{getLocaleProperty('transQty')}" /></td>
								<td><s:property value="%{getLocaleProperty('receiveBags')}" /></td>
								<td><s:property value="%{getLocaleProperty('receQty')}" /></td>
								<td><s:property value="%{getLocaleProperty('heapName')}" /></td>
							</s:if>
							<s:else>
								<td><s:property value="%{getLocaleProperty('farmerCode')}" /></td>
								<td><s:property value="%{getLocaleProperty('farmer')}" /></td>
								<td><s:property value="%{getLocaleProperty('city.name')}" /></td>
								<td><s:property value="%{getLocaleProperty('village')}" /></td>
								<td><s:property value="%{getLocaleProperty('samithi')}" /></td>
								<td><s:property value="%{getLocaleProperty('product')}" /></td>
								<td><s:property value="%{getLocaleProperty('variety')}" /></td>
								<td><s:property value="%{getLocaleProperty('grade')}" /></td>
								<td><s:property value="%{getLocaleProperty('grossWeight')}" /></td>
							</s:else>
							</tr>
						</thead>
						<tbody id="detailDataBody">
						</tbody>
					</div>
				</table>

				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonDataCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>
</body>
