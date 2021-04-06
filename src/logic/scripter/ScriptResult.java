package logic.scripter;

public class ScriptResult {

	private String code;
	private String result;

	public ScriptResult(String code, String result) {
		this.code = code;
		this.result = result;
	}

	public String getCode() {
		return this.code;
	}

	public String getResult() {
		return this.result;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("");
		for (String resultLine : this.getResult().split("\n")) {
			res.append("#" + resultLine + "\n");
		}
		return res.append(this.getCode() + "\n").toString();
	}
}