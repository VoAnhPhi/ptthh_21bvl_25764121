package lab3.src.thuchanh2;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextFileDemo {
	public static void main(String[] args) {
		Path file = Path.of("src", "lab3", "data", "ghi_chu.txt");

		try {
			// tạo thư mục nếu nó chưa tồn tại
			Files.createDirectories(file.getParent());
			// ghi file
			try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
				writer.write("Java I/O làm việc với các luồng dữ liệu.");
				writer.newLine();
				writer.write("BufferedWriter giúp ghi văn bản hiệu quả.");
				writer.newLine();
				writer.write("UTF-8 hỗ trợ tiếng Việt ổn định.");
			}
			// đọc file
			try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
				String line;
				int number = 1;

				while ((line = reader.readLine()) != null) {

					System.out.println(
							number + ". " + line);

					number++;
				}
			}
		} catch (Exception e) {
			System.out.println(
					"Lỗi xử lý file: "
							+ e.getMessage());
		}

	}
}