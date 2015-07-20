# O2OTCP
点纳科技—微信后端
初始化客户端
	var client = hprose.Client.create('tcp://localhost:xxxx/', []); 	
	var proxy = client.useService(['payRequestJson','jsConfig']);
