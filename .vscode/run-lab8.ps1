param(
    [Parameter(Mandatory = $true)]
    [string] $MainClass   # vi du: lab8.bai01.NhanVienDemo
)

# Bien dich va chay bai Lab 8 (JDBC + SQLite) theo module path: bin (code) + lib (sqlite-jdbc, slf4j)
$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

$sources = Get-ChildItem -Path "src/lab8" -Filter *.java -Recurse | ForEach-Object { $_.FullName }
& javac --release 25 -encoding UTF-8 --module-path lib -d bin src/module-info.java @sources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

& java --enable-native-access=org.xerial.sqlitejdbc --add-modules org.slf4j.nop `
    --module-path "bin;lib" --module "PT_HT_TH/$MainClass"
exit $LASTEXITCODE
