package lab3.src.thuchanh1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConsoleReaderDemo {
	public static void main(String[] args) {

		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				System.in,
				StandardCharsets.UTF_8
			)
		);

		int count = 0;
		System.out.println("Nhập văn bản; nhấn q để kết thúc: ");

		try {
			while(true) {
				String line = reader.readLine();

				if (line == null || line.equalsIgnoreCase("q")) break;
				count++;

				System.out.println("Dòng " + count + ": " + line);
			}
		} catch (IOException e) {
			System.out.println(
                    "Không thể đọc dữ liệu: "
                            + e.getMessage()
            );
		}
		System.out.println("Tổng số dòng đã nhập: " + count);
	}
}
