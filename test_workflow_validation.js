const fs = require('fs');
const path = require('path');

/**
 * Test file to validate workflow.txt contains required frontend development standards
 */

function readWorkflowFile() {
  const workflowPath = path.join(__dirname, 'workflow.txt');
  if (!fs.existsSync(workflowPath)) {
    throw new Error('workflow.txt does not exist');
  }
  return fs.readFileSync(workflowPath, 'utf-8');
}

function testSectionExists(content, sectionName) {
  const sectionRegex = new RegExp(`##\\s+4\\.\\s*前端开发规范`, 'i');
  if (!sectionRegex.test(content)) {
    throw new Error(`Missing required section: ${sectionName}`);
  }
  return true;
}

function testDirectoryStructure(content) {
  const hasDirectoryStructure = content.includes('ruoyi-ui/src/') &&
    content.includes('api/library/') &&
    content.includes('views/library/');
  if (!hasDirectoryStructure) {
    throw new Error('Missing ruoyi-ui/src directory structure');
  }
  return true;
}

function testReaderJsApi(content) {
  const hasReaderJs = content.includes('reader.js') &&
    content.includes('export function listReader') &&
    content.includes('export function getReader') &&
    content.includes('export function addReader');
  if (!hasReaderJs) {
    throw new Error('Missing reader.js API examples');
  }
  return true;
}

function testVuePageTemplate(content) {
  const hasVueTemplate = content.includes('<template>') &&
    content.includes('el-table') &&
    content.includes('el-form') &&
    content.includes('el-button');
  if (!hasVueTemplate) {
    throw new Error('Missing Vue page template examples');
  }
  return true;
}

function runTests() {
  console.log('Starting workflow.txt validation tests...\n');
  
  let content;
  try {
    content = readWorkflowFile();
    console.log('✓ Successfully read workflow.txt');
  } catch (error) {
    console.log('✗ Failed to read workflow.txt:', error.message);
    process.exit(1);
  }

  const tests = [
    { name: 'Section 4 exists', test: () => testSectionExists(content, '## 4. 前端开发规范') },
    { name: 'Directory structure defined', test: () => testDirectoryStructure(content) },
    { name: 'Reader.js API examples', test: () => testReaderJsApi(content) },
    { name: 'Vue page template', test: () => testVuePageTemplate(content) }
  ];

  let passed = 0;
  let failed = 0;

  for (const test of tests) {
    try {
      test.test();
      console.log(`✓ ${test.name}`);
      passed++;
    } catch (error) {
      console.log(`✗ ${test.name}: ${error.message}`);
      failed++;
    }
  }

  console.log(`\nTest Results: ${passed} passed, ${failed} failed`);
  return failed === 0;
}

if (require.main === module) {
  const success = runTests();
  process.exit(success ? 0 : 1);
}

module.exports = { runTests };
