class NodeList {
	private String[] ip;
	public void setIP(int i, String value) {
		ip[i]=value;
	}
	public String getIP(int i) {
		return ip[i];
	}
	public NodeList(String[] ip) {
		this.ip=ip;
	}
}
