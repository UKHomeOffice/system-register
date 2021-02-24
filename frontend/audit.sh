#!/usr/bin/env sh

# workaround for missing feature
# https://github.com/yarnpkg/yarn/issues/6669

script_path=`dirname "$0"`
audit_output_file=`mktemp -q`

cd "${script_path}"
yarn audit --json > "${audit_output_file}"
result="$?"

if [ "$result" -eq 0 ]; then
	rm "${audit_output_file}"
	exit 0
fi

if [ -f yarn-audit-known-issues ] && grep auditAdvisory "${audit_output_file}" | diff -q yarn-audit-known-issues - > /dev/null 2>&1; then
  rm "${audit_output_file}"
  echo
  echo Ignoring known vulnerabilities
  exit 0
fi

rm "${audit_output_file}"

echo
echo Security vulnerabilities were found that were not ignored
echo
echo Check to see if these vulnerabilities apply to production
echo and/or if they have fixes available. If they do not have
echo fixes and they do not apply to production, you may ignore them
echo
echo To ignore these vulnerabilities, run:
echo
echo '  yarn audit --json | grep auditAdvisory > yarn-audit-known-issues'
echo
echo and commit the yarn-audit-known-issues file

exit "${result}"
