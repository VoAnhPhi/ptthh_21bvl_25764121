# Lab 4 - Java Socket TCP/UDP

Project Eclipse/VS Code dùng thư mục `src` ở gốc repository làm source root. Vì vậy các file Java của lab nằm trực tiếp dưới `src/lab4`, và tên package phải khớp với đường dẫn tính từ `src`.

```text
src/lab4/
├── guided/                   # Hoạt động thực hành có hướng dẫn
│   ├── network/              # HostInspector
│   └── tcp/                  # TcpCommandServer, TcpCommandClient
├── exercises/                # Bài tập 1-10
│   ├── ex01/                 # Host và URI Inspector
│   ├── ex02/                 # TCP đổi chữ số thành chữ
│   ├── ex03/                 # Dịch vụ ngày giờ TCP và UDP
│   ├── ex04/                 # Máy tính từ xa (sẽ thêm)
│   ├── ex05/                 # Chat nhiều client (sẽ thêm)
│   ├── ex06/                 # So sánh TCP và UDP (sẽ thêm)
│   ├── ex07/                 # Nhật ký tin nhắn (sẽ thêm)
│   ├── ex08/                 # Truyền file an toàn (sẽ thêm)
│   ├── ex09/                 # Multicast thông báo (sẽ thêm)
│   └── ex10/                 # UDP discovery kết hợp TCP (sẽ thêm)
└── evidence/                 # Kết quả kiểm thử và ảnh minh chứng (sẽ thêm)
```

Các thư mục `ex04` đến `ex10` và `evidence` sẽ được tạo khi bắt đầu thực hiện bài tương ứng. Mỗi bài có package riêng, ví dụ `lab4.exercises.ex04`, để các lớp `Server` và `Client` không lẫn với ví dụ hoặc bài khác.

## Biên dịch và chạy

Chạy lệnh từ gốc repository. Ví dụ với bài 2:

```powershell
javac -encoding UTF-8 -d bin src/lab4/exercises/ex02/DigitToWordServer.java src/lab4/exercises/ex02/DigitToWordClient.java
java -cp bin lab4.exercises.ex02.DigitToWordServer
# Mở terminal khác:
java -cp bin lab4.exercises.ex02.DigitToWordClient
```

Trong Eclipse hoặc VS Code, chạy trực tiếp hàm `main` của lớp cần dùng. Với bài client/server, chạy server trước rồi mới chạy client.
