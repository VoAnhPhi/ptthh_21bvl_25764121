param(
    [Parameter(Mandatory = $true)]
    [string] $SourceFile
)

$projectRoot = Split-Path -Parent $PSScriptRoot
$sourceRoot = Join-Path $projectRoot "src"
$outputDirectory = Join-Path $projectRoot "bin"
$resolvedSourceFile = (Resolve-Path -LiteralPath $SourceFile).Path

New-Item -ItemType Directory -Path $outputDirectory -Force | Out-Null

& javac --release 25 -encoding UTF-8 -sourcepath $sourceRoot -d $outputDirectory $resolvedSourceFile
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

$source = Get-Content -LiteralPath $resolvedSourceFile -Raw
$className = [System.IO.Path]::GetFileNameWithoutExtension($resolvedSourceFile)
$packageMatch = [regex]::Match(
    $source,
    '(?m)^\s*package\s+([A-Za-z_][A-Za-z0-9_]*(?:\.[A-Za-z_][A-Za-z0-9_]*)*)\s*;'
)

if ($packageMatch.Success) {
    $qualifiedClassName = "$($packageMatch.Groups[1].Value).$className"
} else {
    $qualifiedClassName = $className
}

& java -cp $outputDirectory $qualifiedClassName
exit $LASTEXITCODE
