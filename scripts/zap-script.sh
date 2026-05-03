#!/usr/bin/env bash
# OWASP ZAP baseline scan against a deployed host (used by .github/workflows/zap-scan.yml).
set -euo pipefail

HOST="${EC2_HOST:-127.0.0.1}"
PORT="${ZAP_TARGET_PORT:-9090}"
TARGET="${ZAP_TARGET_URL:-http://${HOST}:${PORT}}"
REPORT_NAME="${ZAP_REPORT_NAME:-zap_baseline_report.html}"
WORKDIR="$(pwd)"

echo "Running ZAP baseline against: ${TARGET}"
echo "Writing report to: ${WORKDIR}/${REPORT_NAME}"

# --user root: avoid permission errors writing the HTML report to the mounted workdir on Linux runners
docker run --rm --user root \
  -v "${WORKDIR}:/zap/wrk/:rw" \
  ghcr.io/zaproxy/zaproxy:stable \
  zap-baseline.py \
  -t "${TARGET}" \
  -r "/zap/wrk/${REPORT_NAME}" \
  -I

echo "ZAP baseline finished."
